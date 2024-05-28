package vn.ecall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.ecall.domain.User;
import vn.ecall.domain.businessmodel.Cart;
import vn.ecall.domain.businessmodel.Product;
import vn.ecall.repository.CartRepository;
import vn.ecall.repository.ProductRepository;
import vn.ecall.repository.UserRepository;
import vn.ecall.security.AuthoritiesConstants;
import vn.ecall.security.SecurityUtils;
import vn.ecall.service.CartService;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private final ProductRepository productRepository;

    private final CartService cartService;

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    public CartResource(ProductRepository productRepository, UserRepository userRepository, CartService cartService, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Cart> getCartByUserId() throws Exception {
        log.debug("REST request get cart by userId");
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new Exception("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        System.out.println("User: " + user);
        Cart cart = cartService.findByUserId(Objects.requireNonNull(user.orElse(null)).getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user.orElse(null));
            cartRepository.save(cart);
            cart = cartService.findByUserId(user.orElse(null).getId());
        }
        return  ResponseEntity
                .ok()
                .body(cart);
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Cart> addToCart (@PathVariable Integer productId) throws Exception {
        Optional<Product> product = productRepository.findOneById(productId);
        log.debug("REST request to add Item to Cart: {}",product);
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new Exception("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        Cart addCart = cartService.addToCart(product.orElse(null), Objects.requireNonNull(user.orElse(null)), 1);
        return ResponseEntity
                .ok()
                .body(addCart);
    }

    //@PutMapping("/update")

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable Integer productId) throws Exception {
        log.debug("REST request to delete Product from cart: {}", productId);
        Optional<Product> product = productRepository.findOneById(productId);
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new Exception("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        cartService.deleteItemsByIdFromCart(
                Objects.requireNonNull(product.orElse(null)),
                Objects.requireNonNull(user.orElse(null))
        );
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/deleteOneItem/{productId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteOneItemFromCartItem(@PathVariable Integer productId) throws Exception {
        log.debug("REST request to delete one Product from cartItem: {}", productId);
        Optional<Product> product = productRepository.findOneById(productId);
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new Exception("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        cartService.deleteAnItemInCart(
                Objects.requireNonNull(product.orElse(null)),
                Objects.requireNonNull(user.orElse(null))
        );
        return ResponseEntity
                .ok()
                .build();
    }
}
