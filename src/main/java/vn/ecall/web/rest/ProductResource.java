package vn.ecall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import vn.ecall.domain.User;
import vn.ecall.domain.businessmodel.Product;
import vn.ecall.repository.ProductRepository;
import vn.ecall.repository.UserRepository;
import vn.ecall.security.AuthoritiesConstants;
import vn.ecall.security.SecurityUtils;
import vn.ecall.service.OrderService;
import vn.ecall.service.ProductService;
import vn.ecall.service.dto.ProductDTO;
import vn.ecall.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class ProductResource {

    private static class ProductResourceException extends RuntimeException {
        private ProductResourceException(String message) {
            super(message);
        }
    }

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
            Arrays.asList("id", "name", "price", "category")
    );

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;

    private final UserRepository userRepository;

    private final OrderService orderService;

    public ProductResource(ProductRepository productRepository, ProductService productService, UserRepository userRepository, OrderService orderService) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    /**
     * Get all products
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all products.
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(Pageable pageable) {
        log.debug("REST request to get all Products");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<ProductDTO> page = productService.getAllProducts(pageable);
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        log.debug("REST request to get a product by its id");
        ProductDTO product = productService
                .getProductById(id)
                .orElseThrow(() -> new ProductResourceException("Product cannot be found"));
        log.debug("Getting product here!");
        CacheControl cacheControl = CacheControl
                .maxAge(0, TimeUnit.SECONDS)
                .cachePrivate()
                .mustRevalidate();
        //if product has been updated
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .cacheControl(cacheControl)
                .body(product);
    }

    // TODO: Upload image to Amazon S3 and get the link of the image uploaded
    @PostMapping(value = "/products/upload-image/{productName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProductImageToAWSS3(
            @PathVariable("productName") String productName,
            @RequestParam("file")MultipartFile file) {
        return ResponseEntity
                .ok()
                .body(productService.uploadProductImage(productName, file));
    }

    /*
    === Method get product list by categoryID ===
        1. Get at least 4 products
        2. Get products by category id
    */
    @GetMapping("/products/suggestion/{id}")
    public ResponseEntity<List<ProductDTO>> getProductsSuggestionForProductDetailPage(@PathVariable Integer id) throws Exception {
        log.debug("REST request to get a list product by category of product id");
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new Exception("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        List<ProductDTO> productsByCategoryName = productService
                .getAllProductByCategories(
                        Objects.requireNonNull(productService
                                .getProductById(id)
                                .orElse(null))
                                .getCategoryId(),
                        Objects.requireNonNull(user.orElse(null))
                );
        return ResponseEntity
                .ok()
                .body(productsByCategoryName);
    }

    /*
    === Method get product suggestion list ===
        1. Get at least 4 products
    */
    @GetMapping("/products/suggestion")
    public ResponseEntity<List<ProductDTO>> getAllProductSuggestion() throws Exception {
        log.debug("REST request to get a list product suggestion");
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new Exception("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        List<ProductDTO> products = productService
                .get48ProductsSuggestion(
                        Objects.requireNonNull(user.orElse(null))
                );
        return ResponseEntity
                .ok()
                .body(products);
    }

    @GetMapping("/products/random4Product")
    public ResponseEntity<List<ProductDTO>> get4ProductRandom() {
        log.debug("REST request to get a list product suggestion");
        List<ProductDTO> four_products = productService.get4Products();
        return ResponseEntity
                .ok()
                .body(four_products);
    }

    @GetMapping("/products/totalPages")
    public ResponseEntity<Integer> getTotalPages(Pageable pageable) {
        log.debug("REST request to get total pages");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<ProductDTO> page = productService.getAllProducts(pageable);
        return new ResponseEntity<>(page.getTotalPages(), HttpStatus.OK);
    }


    /**
     * Get all products by theirs category
     *
     * @param id:       category id
     * @param pageable: the pagination information
     * @return
     */
    @GetMapping("/products/category/{id}")
    public ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@PathVariable Integer id, Pageable pageable) {
        log.debug("REST request to get all Products by category");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<ProductDTO> page = productService.getAllProductByCategory(id, pageable);
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
    }

    @GetMapping("/products/category/totalPages/{id}")
    public ResponseEntity<Integer> getTotalProductPagesByCategory(@PathVariable Integer id, Pageable pageable) {
        log.debug("REST request to total pages by category");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<ProductDTO> page = productService.getAllProductByCategory(id, pageable);
        return new ResponseEntity<>(page.getTotalPages(), HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProductByName(@RequestParam String searchText) {
        log.debug("REST request to search for products by name");
        final List<Product> products = this.productService.searchProductsByName(searchText);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code POST /admin/products} : Create new product
     *
     * @param product product to be saved
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/provider/products")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.PROVIDER + "\")")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to create Product: {}", product);
        System.out.println("PRODUCT GET: " + product);

        if (productService.getProductByName(product.getName()).isPresent()) {
            throw new BadRequestAlertException("Product name already used", "productManagement", "productexists");
        } else {
            Product newProduct = productService.createProduct(product);
            return ResponseEntity
                    .created(new URI("api/provider/products/" + product.getId())) //this need to be checked
                    .body(newProduct);
        }
    }

    /**
     * {@code PUT /provider/products} Update product information
     *
     * @param product product to be update
     * @return update product
     * @throws ResponseStatusException
     */
    @PutMapping("/provider/products")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.PROVIDER + "\")")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) {
        log.debug("REST request to update Product: {}", product);
        ProductDTO productForCheckingVersion = productService
                .getProductById(product.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        CacheControl cacheControl = CacheControl.maxAge(0, TimeUnit.SECONDS).cachePrivate().mustRevalidate();

        Optional<Product> updateProduct = productService.updateProduct(product);
        return ResponseEntity
                .ok()
                .cacheControl(cacheControl)
                .body(updateProduct.get());

    }

    /**
     * {@code DELETE /provider/products/:id}: delete product with this id.
     *
     * @param id the id of the product to delete
     * @return an empty entity
     */
    @DeleteMapping("/provider/products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.PROVIDER + "\")")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        log.debug("REST request to delete Product: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
