package com.prm.manzone.mapper;

import com.prm.manzone.entities.Category;
import com.prm.manzone.payload.category.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
