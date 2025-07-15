package com.prm.manzone.exception;

public enum ErrorCode {
    // Order related errors
    ORDER_NOT_FOUND("Order not found"),
    PRODUCT_NOT_FOUND("Product not found"),
    ACCESS_DENIED("Access denied"),
    CANNOT_CANCEL_ORDER("Cannot cancel order"),
    CANNOT_UPDATE_ORDER("Cannot update order"),
    INVALID_STATUS_TRANSITION("Invalid status transition"),

    // User related errors
    USER_NOT_FOUND("User not found"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    INVALID_PHONE_NUMBER("Invalid phone number"),
    CANNOT_DELETE_ADMIN("Cannot delete admin user"),
    CANNOT_DEACTIVATE_ADMIN("Cannot deactivate admin user"),
    USER_ALREADY_DELETED("User is already deleted"),
    USER_ALREADY_ACTIVATED("User is already activated"),
    USER_ALREADY_DEACTIVATED("User is already deactivated"),

    // General errors
    INVALID_REQUEST("Invalid request"),
    INTERNAL_SERVER_ERROR("Internal server error");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
