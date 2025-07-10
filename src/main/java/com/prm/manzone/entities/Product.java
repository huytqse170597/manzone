package com.prm.manzone.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
public class Product extends BaseEntity {
    @Column(nullable = false)
    String name;

    @Column(length = 1000)
    String description;

    @Column(nullable = false)
    Double price;

    @Column(name = "image_urls")
    List<String> imageUrls;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
}
