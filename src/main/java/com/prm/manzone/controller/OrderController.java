package com.prm.manzone.controller;

import com.prm.manzone.enums.OrderStatus;
import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.payload.order.*;
import com.prm.manzone.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "API for managing orders")
public class OrderController {

        private final IOrderService orderService;

        @Operation(summary = "Create a new order")
        @PostMapping
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_CUSTOMER')")
        public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
                        @Valid @RequestBody CreateOrderRequest request) {
                OrderResponse response = orderService.createOrder(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.<OrderResponse>builder()
                                                .success(true)
                                                .message("Order created successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Get my orders")
        @GetMapping("/my-orders")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_CUSTOMER')")
        public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrders(
                        @RequestParam(required = false) Integer page,
                        @RequestParam(required = false) Integer size) {
                Page<OrderResponse> response = orderService.getMyOrders(page, size);
                return ResponseEntity.ok(
                                ApiResponse.<Page<OrderResponse>>builder()
                                                .success(true)
                                                .message("Orders retrieved successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Get my order by ID")
        @GetMapping("/my-orders/{id}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_CUSTOMER')")
        public ResponseEntity<ApiResponse<OrderResponse>> getMyOrderById(
                        @PathVariable Integer id) {
                OrderResponse response = orderService.getMyOrderById(id);
                return ResponseEntity.ok(
                                ApiResponse.<OrderResponse>builder()
                                                .success(true)
                                                .message("Order retrieved successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Cancel my order")
        @PutMapping("/my-orders/{id}/cancel")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_CUSTOMER')")
        public ResponseEntity<ApiResponse<Void>> cancelOrder(
                        @PathVariable Integer id) {
                orderService.cancelOrder(id);
                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .success(true)
                                                .message("Order cancelled successfully")
                                                .build());
        }

        @Operation(summary = "Get my orders by status")
        @GetMapping("/my-orders/status/{status}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_CUSTOMER')")
        public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrdersByStatus(
                        @PathVariable OrderStatus status) {
                List<OrderResponse> response = orderService.getMyOrdersByStatus(status);
                return ResponseEntity.ok(
                                ApiResponse.<List<OrderResponse>>builder()
                                                .success(true)
                                                .message("Orders retrieved successfully")
                                                .data(response)
                                                .build());
        }

        // Admin APIs
        @Operation(summary = "Get all orders (Admin)")
        @GetMapping("/admin")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
                        @RequestParam(required = false) Integer page,
                        @RequestParam(required = false) Integer size,
                        @RequestParam(required = false) OrderStatus status) {
                Page<OrderResponse> response = orderService.getAllOrders(page, size, status);
                return ResponseEntity.ok(
                                ApiResponse.<Page<OrderResponse>>builder()
                                                .success(true)
                                                .message("Orders retrieved successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Get order by ID (Admin)")
        @GetMapping("/admin/{id}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Integer id) {
                OrderResponse response = orderService.getOrderById(id);
                return ResponseEntity.ok(
                                ApiResponse.<OrderResponse>builder()
                                                .success(true)
                                                .message("Order retrieved successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Update order status (Admin)")
        @PutMapping("/admin/{id}/status")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
                        @PathVariable Integer id,
                        @Valid @RequestBody UpdateOrderStatusRequest request) {
                OrderResponse response = orderService.updateOrderStatus(id, request);
                return ResponseEntity.ok(
                                ApiResponse.<OrderResponse>builder()
                                                .success(true)
                                                .message("Order status updated successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Update order details (Admin)")
        @PutMapping("/admin/{id}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
                        @PathVariable Integer id,
                        @Valid @RequestBody UpdateOrderRequest request) {
                OrderResponse response = orderService.updateOrder(id, request);
                return ResponseEntity.ok(
                                ApiResponse.<OrderResponse>builder()
                                                .success(true)
                                                .message("Order updated successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Delete order (Admin)")
        @DeleteMapping("/admin/{id}")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Integer id) {
                orderService.deleteOrder(id);
                return ResponseEntity.ok(
                                ApiResponse.<Void>builder()
                                                .success(true)
                                                .message("Order deleted successfully")
                                                .build());
        }

        @Operation(summary = "Get order statistics")
        @GetMapping("/admin/statistics")
        @SecurityRequirement(name = "Bearer Authentication")
        @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
        public ResponseEntity<ApiResponse<OrderStatisticsResponse>> getOrderStatistics() {
                long totalPending = orderService.getTotalOrdersByStatus(OrderStatus.PENDING);
                long totalConfirmed = orderService.getTotalOrdersByStatus(OrderStatus.CONFIRMED);
                long totalShipped = orderService.getTotalOrdersByStatus(OrderStatus.SHIPPED);
                long totalDelivered = orderService.getTotalOrdersByStatus(OrderStatus.DELIVERED);
                long totalCancelled = orderService.getTotalOrdersByStatus(OrderStatus.CANCELLED);

                OrderStatisticsResponse statistics = OrderStatisticsResponse.builder()
                                .pending(totalPending)
                                .confirmed(totalConfirmed)
                                .shipped(totalShipped)
                                .delivered(totalDelivered)
                                .cancelled(totalCancelled)
                                .total(totalPending + totalConfirmed + totalShipped + totalDelivered + totalCancelled)
                                .build();

                return ResponseEntity.ok(
                                ApiResponse.<OrderStatisticsResponse>builder()
                                                .success(true)
                                                .message("Order statistics retrieved successfully")
                                                .data(statistics)
                                                .build());
        }
}
