package com.prm.manzone.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(int id) {
        super("Category with id " + id + " not found");
    }
}
