package com.prm.manzone.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Column(nullable = false)
    String name;

    @Column(length = 1000)
    String description;

    @Column(nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tiền phải lớn hơn 0")
    BigDecimal price;

    @Column(name = "image_urls")
    List<String> imageUrls;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
}
