package com.prm.manzone.mapper;

import com.prm.manzone.entities.Order;
import com.prm.manzone.entities.OrderDetail;
import com.prm.manzone.payload.order.OrderDetailResponse;
import com.prm.manzone.payload.order.OrderResponse;
import com.prm.manzone.payload.user.response.UserDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final UserMapper userMapper;

    public OrderMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderDetailResponse> orderDetailResponses = null;
        if (order.getOrderDetails() != null) {
            orderDetailResponses = order.getOrderDetails().stream()
                    .map(this::toOrderDetailResponse)
                    .collect(Collectors.toList());
        }

        UserDTO userDTO = userMapper.toUserDTO(order.getUser());

        return OrderResponse.builder()
                .id(order.getId())
                .user(userDTO)
                .orderDetails(orderDetailResponses)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .phoneNumber(order.getPhoneNumber())
                .shippingAddress(order.getShippingAddress())
                .note(order.getNote())
                .customerName(order.getCustomerName())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }

        BigDecimal subtotal = orderDetail.getPrice()
                .multiply(BigDecimal.valueOf(orderDetail.getQuantity()));

        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .productId(orderDetail.getProduct().getId())
                .productName(orderDetail.getProductName())
                .productImageUrl(orderDetail.getProductImageUrl())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .subtotal(subtotal)
                .build();
    }

    public List<OrderResponse> toOrderResponseList(List<Order> orders) {
        if (orders == null) {
            return null;
        }

        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
}
