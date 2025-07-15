package com.prm.manzone.payload.order;

import com.prm.manzone.enums.OrderStatus;
import com.prm.manzone.payload.user.response.UserDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer id;
    UserDTO user;
    List<OrderDetailResponse> orderDetails;
    BigDecimal totalAmount;
    OrderStatus status;
    String phoneNumber;
    String shippingAddress;
    String note;
    String customerName;
    Instant createdAt;
    Instant updatedAt;
}
