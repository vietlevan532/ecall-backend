package vn.ecall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.ecall.domain.User;
import vn.ecall.domain.businessmodel.Order;
import vn.ecall.repository.CartRepository;
import vn.ecall.repository.UserRepository;
import vn.ecall.security.AuthoritiesConstants;
import vn.ecall.security.SecurityUtils;
import vn.ecall.service.CartService;
import vn.ecall.service.OrderService;
import vn.ecall.service.dto.OrderDTO;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/order")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private final OrderService orderService;

    private final UserRepository userRepository;

    private final CartService cartService;

    public OrderResource(OrderService orderService, UserRepository userRepository, CartRepository cartRepository, CartService cartService) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }
    private static class OrderResourceException extends RuntimeException {
        private OrderResourceException(String message) {
            super(message);
        }
    }

    @GetMapping("/list-order")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SALE + "\")")
    public ResponseEntity<List<Order>> getOrderList() {
        log.debug("REST request get list of order");
        List<Order> orders = orderService.getAllOrder();
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return ResponseEntity
                .ok()
                .body(orders);
    }

    @GetMapping("/order-detail/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SALE + "\")")
    public ResponseEntity<Order> getOrderByUserId( @PathVariable Integer id) {
        log.debug("REST request to get a order by its id");
        Order order = orderService
                .getOrderById(id)
                .orElseThrow(() -> new OrderResourceException("Order cannot be found"));
        log.debug("Getting order here!");
        CacheControl cacheControl = CacheControl
                .maxAge(0, TimeUnit.SECONDS)
                .cachePrivate()
                .mustRevalidate();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .cacheControl(cacheControl)
                .body(order);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Order> updateOrderStatus(
            @RequestBody OrderDTO orderDTO,
            @PathVariable Integer id) {
        Order orderStatusUpdate = orderService.updateOrderStatus(orderDTO, id);
        return ResponseEntity
                .ok()
                .body(orderStatusUpdate);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<OrderDTO> createAnOrderByUser(@RequestBody OrderDTO orderDTO) {
        System.out.println("ORDER: " + orderDTO.getOrderDetails());
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .get();
        log.debug("REST request to create an order by user: {}", userLogin);
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        orderService.createOrder(orderDTO, user.orElse(null));
        cartService.deleteAllItemsFromCart(Objects.requireNonNull(user.orElse(null)));
        return ResponseEntity
                .ok()
                .body(orderDTO);
    }
}
