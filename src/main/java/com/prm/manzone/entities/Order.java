package com.prm.manzone.entities;

import com.prm.manzone.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class Order extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @NotNull(message = "User là bắt buộc")
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<OrderDetail> orderDetails;

    @Column(nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Tổng tiền phải lớn hơn 0")
    @NotNull(message = "Tổng tiền là bắt buộc")
    BigDecimal totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Trạng thái đơn hàng là bắt buộc")
    @Builder.Default
    OrderStatus status = OrderStatus.PENDING;

    @NotEmpty(message = "Số điện thoại là bắt buộc")
    @Pattern(regexp = "^(0|\\+84|84)([35789])[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    @Column(nullable = false)
    String phoneNumber;

    @NotEmpty(message = "Địa chỉ giao hàng là bắt buộc")
    @Column(nullable = false, length = 500)
    String shippingAddress;

    @Column(length = 1000)
    String note;

    @Column(name = "customer_name", nullable = false)
    @NotEmpty(message = "Tên khách hàng là bắt buộc")
    String customerName;
}
