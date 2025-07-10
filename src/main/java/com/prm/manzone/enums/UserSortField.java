package com.prm.manzone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum UserSortField {
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    ROLE("role");

    private String fieldName;

    UserSortField(String fieldName) {
        this.fieldName = fieldName;
    }

}
