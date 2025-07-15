package com.prm.manzone.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@SQLRestriction("deleted = false")
public class Category extends BaseEntity {
    @Column(nullable = false, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci", unique = true)
    @NotEmpty(message = "Tên loại sản phẩm là bắt buộc")
    String name;

    @Column(nullable = false)
    String description;
}
