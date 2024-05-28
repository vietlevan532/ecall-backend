package vn.ecall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ecall.domain.User;
import vn.ecall.domain.businessmodel.Cart;
import vn.ecall.domain.businessmodel.CartItem;
import vn.ecall.domain.businessmodel.Product;
import vn.ecall.repository.CartItemRepository;
import vn.ecall.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // Method add item to cart
    @Transactional
    public Cart addToCart(Product product, User user, int quantity) {
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
        }
        List<CartItem> cartItemList = cart.getCartItems();
        CartItem cartItem = findCartItem(cartItemList, product.getId());
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(quantity * Double.parseDouble(product.getPrice()));
            cartItem.setCart(cart);
            cartItemList.add(cartItem);
        } else {
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * Double.parseDouble(product.getPrice()));
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItemList.add(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * Double.parseDouble(product.getPrice())));
            }
            cartItemRepository.save(cartItem);
        }
        cart.setCartItems(cartItemList);
        cart.setTotalItems(totalItems(cart.getCartItems()));
        cart.setTotalPrice(totalPrices(cart.getCartItems()));
        cart.setUser(user);
        log.debug("Add to cart: {}", cart);
        return cartRepository.save(cart);
    }

    //Method update item in cart
    @Transactional
    public void updateItemInCart(Product product, User user, int quantity) {
        Cart cart = user.getCart();
        List<CartItem> cartItemList = cart.getCartItems();
        CartItem item = findCartItem(cartItemList, product.getId());

        item.setQuantity(quantity);
        item.setTotalPrice(quantity * (Integer.parseInt(product.getPrice())));
        cartItemRepository.save(item);

        cart.setTotalItems(totalItems(cartItemList));
        cart.setTotalPrice(totalPrices(cartItemList));
        cart.setUser(user);
        cartRepository.save(cart);
    }
    
    //Method delete all items in cart
    @Transactional
    public void deleteAllItemsFromCart(User user) {
        Cart cart = user.getCart();
        List<CartItem> cartItemList = cart.getCartItems();
        for (CartItem item : cartItemList) {
            cartItemRepository.delete(item);
        }
        cart.setCartItems(new ArrayList<>());
        cart.setTotalItems(0);
        cart.setTotalPrice(0);
        cart.setUser(user);
        cartRepository.save(cart);
    }

    //Method delete item form cart
    @Transactional
    public void deleteItemsByIdFromCart(Product product, User user) {
        Cart cart = user.getCart();
        List<CartItem> cartItemList = cart.getCartItems();
        CartItem item = findCartItem(cartItemList, product.getId());

        cartItemList.remove(item);
        cartItemRepository.delete(item);

        cart.setCartItems(cartItemList);
        cart.setTotalItems(totalItems(cartItemList));
        cart.setTotalPrice(totalPrices(cartItemList));
        cart.setUser(user);
        cartRepository.save(cart);
    }

    // Method remove an item in cart
    public void deleteAnItemInCart(Product product, User user) {
        Cart cart = user.getCart();
        List<CartItem> cartItemList = cart.getCartItems();
        CartItem item = findCartItem(cartItemList, product.getId());

        item.setQuantity(item.getQuantity() - 1);
        item.setTotalPrice(item.getQuantity() * Double.parseDouble(product.getPrice()));
        cartItemRepository.save(item);

        cart.setCartItems(cartItemList);
        cart.setTotalItems(totalItems(cartItemList));
        cart.setTotalPrice(totalPrices(cartItemList));
        cart.setUser(user);
        cartRepository.save(cart);
    }

    public Cart findByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    // Method find Cart Item
    private CartItem findCartItem(List<CartItem> cartItemList, Integer productId) {
        if (cartItemList == null) { return null; }
        CartItem cartItem = null;
        for (CartItem item : cartItemList) {
            if (Objects.equals(item.getProduct().getId(), productId)) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    // Method calculate total items in list cart item
    private int totalItems (List<CartItem> cartItemList) {
        int totalItems = 0;
        for (CartItem item : cartItemList) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    // Method calculate total prices in list cart item
    private double totalPrices (List<CartItem> cartItemList) {
        double totalPrices = 0.0;
        for (CartItem item : cartItemList) {
            totalPrices += item.getTotalPrice();
        }
        return totalPrices;
    }

}
