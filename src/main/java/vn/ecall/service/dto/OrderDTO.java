package vn.ecall.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.ecall.domain.User;
import vn.ecall.domain.businessmodel.OrderDetail;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private Integer id;
    private Date orderDate;
    private double totalPrice;
    private int totalQuantity;
    private String orderStatus;
    private String fullName;
    private String phoneNumber;
    private String city;
    private String district;
    private String detailAddress;
    private String email;
    private String notes;
    private User user;
    private List<OrderDetail> orderDetails;
}
