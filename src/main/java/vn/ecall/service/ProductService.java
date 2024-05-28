package vn.ecall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.ecall.a3.AmazonS3Service;
import vn.ecall.a3.BucketName;
import vn.ecall.domain.User;
import vn.ecall.domain.businessmodel.Order;
import vn.ecall.domain.businessmodel.OrderDetail;
import vn.ecall.domain.businessmodel.Product;
import vn.ecall.repository.ProductRepository;
import vn.ecall.service.dto.ProductDTO;
import vn.ecall.web.rest.errors.BadRequestAlertException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final AmazonS3Service amazonS3Service;

    public ProductService(ProductRepository productRepository, AmazonS3Service amazonS3Service) {
        this.productRepository = productRepository;
        this.amazonS3Service = amazonS3Service;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProductByCategory(Integer categoryId, Pageable pageable){
        return productRepository.findAllByCategoryId(categoryId, pageable).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProductByCategories(Integer categoryId, User user){
        List<ProductDTO> productsSuggestion = productRepository.findAllByCategory_categoryId(categoryId)
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        List<ProductDTO> products = productRepository.findAll()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        if (!user.getOrders().isEmpty()) {
            for (Order order : user.getOrders()) {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    for (ProductDTO product : productRepository.
                            findAllByCategory_categoryId(
                                    orderDetail
                                            .getProduct()
                                            .getCategory()
                                            .getCategory_id()
                            )
                            .stream()
                            .map(ProductDTO::new)
                            .collect(Collectors.toList())
                    ) {
                        if (!products.contains(product)) {
                            products.add(0, product);
                        }
                    }
                }
            }
        }
        if (productsSuggestion.size() > 48) {
            if (productsSuggestion.size() % 4 != 0) {
                int excessItems = productsSuggestion.size() % 4;
                int startIndex = productsSuggestion.size() - excessItems;
                productsSuggestion.subList(startIndex, productsSuggestion.size()).clear();
            } else {
                return productsSuggestion;
            }
        } else if (productsSuggestion.size() < 48){
            for (ProductDTO product : products) {
                if (productsSuggestion.size() % 4 != 0) {
                    if (!productsSuggestion.contains(product)) {
                        productsSuggestion.add(product);
                    }
                } else {
                    break;
                }
            }
        } else {
            return productsSuggestion;
        }
        return productsSuggestion;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> get48ProductsSuggestion(User user) {
        List<ProductDTO> products =  productRepository.findAll()
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        if (!user.getOrders().isEmpty()) {
            for (Order order : user.getOrders()) {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    for (ProductDTO product : productRepository.
                            findAllByCategory_categoryId(
                                    orderDetail
                                            .getProduct()
                                            .getCategory()
                                            .getCategory_id()
                            )
                            .stream()
                            .map(ProductDTO::new)
                            .collect(Collectors.toList())
                    ) {
                        if (!products.contains(product)) {
                            products.add(0, product);
                        }
                    }
                }
            }
        }
        if (products.size() != 48) {
            if (products.size() % 4 != 0) {
                if (products.size() > 48) {
                    Collections.shuffle(products);
                    return products.subList(0, 48);
                } else {
                    int excessItems = products.size() % 4;
                    int startIndex = products.size() - excessItems;
                    products.subList(startIndex, products.size()).clear();
                    return products;
                }
            } else {
                return products;
            }
        } else {
            return products;
        }
    }

    public List<Product> searchProductsByName(String searchText) {
        return this.productRepository.findByNameContaining(searchText);
    }

    public Optional<ProductDTO> getProductById(Integer id) {
        return productRepository
                .findOneById(id)
                .map(ProductDTO::new);
    }

    public Optional<Product> getProductByName(String name) {
        return productRepository.findOneByName(name.toLowerCase());
    }

    public Product createProduct(Product product) {
        /*Product newProduct = new Product();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setCategory(product.getCategory());
        newProduct.setShortDes(product.getShortDes());
        newProduct.setFullDes(product.getFullDes());
        newProduct.setImageLink(product.getImageLink());*/
        Product newProduct = productRepository.save(product);
        log.debug("Created Information for Product: {}", product);
        return productRepository.save(newProduct);
    }

    public void deleteProduct(Integer id) {
        productRepository
                .findOneById(id)
                .ifPresent(
                        product -> {
                            productRepository.delete(product);
                            log.debug("Deleted Product: {}", product);
                        }
                );
    }

    @Transactional
    public Optional<Product> updateProduct(Product product) {
        return Optional
                .of(productRepository.findOneById(product.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(
                        updateProduct -> {
                            updateProduct.setName(product.getName());
                            updateProduct.setCategory(product.getCategory());
                            updateProduct.setPrice(product.getPrice());
                            updateProduct.setShortDes(product.getShortDes());
                            updateProduct.setFullDes(product.getFullDes());
                            updateProduct.setImageLink(product.getImageLink());
                            log.debug("Changed Information for Product: {}", updateProduct);
                            return updateProduct;
                        }
                );
    }

    // TODO: Upload image to AWS S3
    public String uploadProductImage(String productName, MultipartFile file) {
        String key = "product/image/" + file.getOriginalFilename();
        try {
            amazonS3Service.putObject(
                    BucketName.BUCKET_NAME,
                    key,
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "https://"
                + BucketName.BUCKET_NAME
                + ".s3.ap-southeast-2.amazonaws.com/"
                + key;
    }

    public List<ProductDTO> get4Products() {
        List<ProductDTO> products = productRepository.findAll()
                .stream()
                .map(ProductDTO :: new)
                .collect(Collectors.toList());
        Collections.shuffle(products);
        return products
                .stream()
                .limit(4)
                .collect(Collectors.toList());
    }
}
