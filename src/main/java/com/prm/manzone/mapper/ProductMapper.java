package com.prm.manzone.mapper;

import com.prm.manzone.entities.Product;
import com.prm.manzone.payload.product.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .description(product.getDescription())
                .name(product.getName())
                .imageUrls(product.getImageUrls())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
