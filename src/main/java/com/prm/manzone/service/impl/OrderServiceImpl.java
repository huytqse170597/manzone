package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Order;
import com.prm.manzone.entities.OrderDetail;
import com.prm.manzone.entities.Product;
import com.prm.manzone.entities.User;
import com.prm.manzone.enums.OrderStatus;
import com.prm.manzone.exception.AppException;
import com.prm.manzone.exception.ErrorCode;
import com.prm.manzone.mapper.OrderMapper;
import com.prm.manzone.payload.order.*;
import com.prm.manzone.repository.OrderDetailRepository;
import com.prm.manzone.repository.OrderRepository;
import com.prm.manzone.repository.ProductRepository;
import com.prm.manzone.repository.UserRepository;
import com.prm.manzone.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    private int extractUserIdFromSecurityContext() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();

            if (context.getAuthentication() == null) {
                throw new RuntimeException("No authentication found in security context");
            }

            if (context.getAuthentication().getName() == null) {
                throw new RuntimeException("Authentication principal is null");
            }

            String principal = context.getAuthentication().getName();
            log.debug("Extracted principal from security context: {}", principal);

            if ("anonymousUser".equals(principal)) {
                throw new RuntimeException("User is not authenticated - found anonymous user");
            }

            return Integer.parseInt(principal);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse user ID from security context: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract user information from security context: " + e.getMessage(),
                    e);
        }
    }

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        int userId = extractUserIdFromSecurityContext();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailRequest detailRequest : request.getOrderDetails()) {
            Product product = productRepository.findByIdAndDeletedFalse(detailRequest.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .quantity(detailRequest.getQuantity())
                    .price(product.getPrice())
                    .productName(product.getName())
                    .productImageUrl(product.getImageUrls() != null && !product.getImageUrls().isEmpty()
                            ? product.getImageUrls().get(0)
                            : null)
                    .build();

            orderDetails.add(orderDetail);
        }

        // Create order
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .phoneNumber(request.getPhoneNumber())
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .customerName(request.getCustomerName())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Set order reference in order details and save
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrder(savedOrder);
        }
        orderDetailRepository.saveAll(orderDetails);

        // Reload order with details
        savedOrder = orderRepository.findByIdAndDeletedFalse(savedOrder.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        savedOrder.setOrderDetails(orderDetails);
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public Page<OrderResponse> getMyOrders(Integer page, Integer size) {
        int userId = extractUserIdFromSecurityContext();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Getting orders for user: {}", user.getEmail());

        int pageNumber = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 ? size : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Order> orders = orderRepository.findByUserAndDeletedFalse(user, pageable);
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse getMyOrderById(Integer id) {
        int userId = extractUserIdFromSecurityContext();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Getting order {} for user: {}", id, user.getEmail());

        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Integer id) {
        int userId = extractUserIdFromSecurityContext();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Cancelling order {} for user: {}", id, user.getEmail());

        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_ORDER);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order {} cancelled successfully", id);
    }

    @Override
    public List<OrderResponse> getMyOrdersByStatus(OrderStatus status) {
        int userId = extractUserIdFromSecurityContext();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        log.info("Getting orders with status {} for user: {}", status, user.getEmail());

        List<Order> orders = orderRepository.findByUserAndStatusAndDeletedFalse(user, status);
        return orderMapper.toOrderResponseList(orders);
    }

    @Override
    public Page<OrderResponse> getAllOrders(Integer page, Integer size, OrderStatus status) {
        log.info("Getting all orders with status: {}", status);

        int pageNumber = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 ? size : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findByStatusAndDeletedFalse(status, pageable);
        } else {
            orders = orderRepository.findByDeletedFalse(pageable);
        }

        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    public OrderResponse getOrderById(Integer id) {
        log.info("Getting order by ID: {}", id);

        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Integer id, UpdateOrderStatusRequest request) {
        log.info("Updating order {} status to: {}", id, request.getStatus());

        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Validate status transition
        validateStatusTransition(order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());
        Order savedOrder = orderRepository.save(order);

        log.info("Order {} status updated successfully", id);
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Integer id, UpdateOrderRequest request) {
        log.info("Updating order: {}", id);

        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.CANNOT_UPDATE_ORDER);
        }

        if (request.getPhoneNumber() != null) {
            order.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getShippingAddress() != null) {
            order.setShippingAddress(request.getShippingAddress());
        }
        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }
        if (request.getCustomerName() != null) {
            order.setCustomerName(request.getCustomerName());
        }

        Order savedOrder = orderRepository.save(order);

        log.info("Order {} updated successfully", id);
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Integer id) {
        log.info("Deleting order: {}", id);

        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setDeleted(true);
        orderRepository.save(order);

        log.info("Order {} deleted successfully", id);
    }

    @Override
    public long getTotalOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public long getUserOrderCountByStatus(User user, OrderStatus status) {
        return orderRepository.countByUserAndStatus(user, status);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case CONFIRMED:
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case SHIPPED:
                if (newStatus != OrderStatus.DELIVERED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case DELIVERED:
            case CANCELLED:
                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }
    }
}
