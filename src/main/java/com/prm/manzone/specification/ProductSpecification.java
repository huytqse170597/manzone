package com.prm.manzone.specification;

import com.prm.manzone.entities.Category;
import com.prm.manzone.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchTerm = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchTerm));
        };
    }
}
