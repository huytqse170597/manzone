package com.prm.manzone.specification;

import com.prm.manzone.entities.User;
import com.prm.manzone.enums.Role;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasSearchString(String searchString) {
        return (root, query, criteriaBuilder) -> {
            if (searchString == null || searchString.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String trimmedSearch = searchString.trim().toLowerCase();
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                            "%" + trimmedSearch + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                            "%" + trimmedSearch + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                            "%" + trimmedSearch + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")),
                            "%" + trimmedSearch + "%"));
        };
    }

    public static Specification<User> hasRole(Role role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    public static Specification<User> hasDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) -> {
            if (deleted == null) {
                // If null, return all users regardless of deleted status
                return criteriaBuilder.conjunction();
            }
            if (deleted) {
                return criteriaBuilder.isTrue(root.get("deleted"));
            } else {
                return criteriaBuilder.isFalse(root.get("deleted"));
            }
        };
    }
}
