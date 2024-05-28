package vn.ecall.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ecall.domain.User;
import vn.ecall.domain.businessmodel.Order;
import vn.ecall.domain.businessmodel.OrderDetail;
import vn.ecall.repository.OrderDetailRepository;
import vn.ecall.repository.OrderRepository;
import vn.ecall.service.dto.OrderDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;


    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, EntityManager entityManager) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public void createOrder(OrderDTO orderDTO, User user){
        Order order = new Order();
        order.setOrderDate(orderDTO.getOrderDate());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setTotalQuantity(orderDTO.getTotalQuantity());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setFullName(orderDTO.getFullName());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setCity(orderDTO.getCity());
        order.setDistrict(orderDTO.getDistrict());
        order.setDetailAddress(orderDTO.getDetailAddress());
        order.setEmail(orderDTO.getEmail());
        order.setNotes(orderDTO.getNotes());
        order.setOrderDetails(orderDTO.getOrderDetails());
        order.setUser(user);
        orderRepository.save(order);
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setOrder(order);
            orderDetailRepository.save(orderDetail);
        }
    }

    public Order updateOrderStatus(OrderDTO orderDTO, Integer id) {
        System.out.println("OrderDTO: " + orderDTO);
        Optional<Order> order = getOrderById(id);
        Objects.requireNonNull(order.orElse(null)).setOrderStatus(orderDTO.getOrderStatus());
        return orderRepository.save(order.orElse(null));
    }

    @Transactional
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    // Find all orders of a User
    @Transactional
    public List<Order> findAllByUserId (Integer userId) {
        return orderRepository.findAllByUserId(userId);
    }

    // Find all orders
    @Transactional
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

}
