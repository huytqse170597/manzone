package com.prm.manzone.service;

import com.prm.manzone.entities.User;
import com.prm.manzone.enums.OrderStatus;
import com.prm.manzone.payload.order.CreateOrderRequest;
import com.prm.manzone.payload.order.OrderResponse;
import com.prm.manzone.payload.order.UpdateOrderRequest;
import com.prm.manzone.payload.order.UpdateOrderStatusRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {

    // Customer APIs
    OrderResponse createOrder(CreateOrderRequest request);

    Page<OrderResponse> getMyOrders(Integer page, Integer size);

    OrderResponse getMyOrderById(Integer id);

    void cancelOrder(Integer id);

    List<OrderResponse> getMyOrdersByStatus(OrderStatus status);

    // Admin APIs
    Page<OrderResponse> getAllOrders(Integer page, Integer size, OrderStatus status);

    OrderResponse getOrderById(Integer id);

    OrderResponse updateOrderStatus(Integer id, UpdateOrderStatusRequest request);

    OrderResponse updateOrder(Integer id, UpdateOrderRequest request);

    void deleteOrder(Integer id);

    // Statistics
    long getTotalOrdersByStatus(OrderStatus status);

    long getUserOrderCountByStatus(User user, OrderStatus status);
}
