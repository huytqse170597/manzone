package com.prm.manzone.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class OrderDetail extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @NotNull(message = "Order là bắt buộc")
    Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    @NotNull(message = "Product là bắt buộc")
    Product product;

    @Column(nullable = false)
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @NotNull(message = "Số lượng là bắt buộc")
    Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @NotNull(message = "Giá là bắt buộc")
    BigDecimal price;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "product_image_url")
    String productImageUrl;
}
