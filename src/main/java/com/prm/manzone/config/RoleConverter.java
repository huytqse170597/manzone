package com.prm.manzone.config;

import com.prm.manzone.enums.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }

        // Handle both "ADMIN" and "ROLE_ADMIN" formats
        String normalizedSource = source.trim().toUpperCase();

        // Remove "ROLE_" prefix if present
        if (normalizedSource.startsWith("ROLE_")) {
            normalizedSource = normalizedSource.substring(5);
        }

        try {
            return Role.valueOf(normalizedSource);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid role: " + source + ". Valid values are: CUSTOMER, ADMIN, ROLE_CUSTOMER, ROLE_ADMIN");
        }
    }
}
