package vn.ecall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.ecall.domain.businessmodel.Order;
import vn.ecall.security.AuthoritiesConstants;
import vn.ecall.service.OrderService;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/sale")
public class SaleResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final OrderService orderService;

    public SaleResource(OrderService orderService) {
        this.orderService = orderService;
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

}
