package com.fooddelivery.fooddeliveryapp.exception;

import org.springframework.http.HttpStatus;

public class EntityExistsException extends ApiException {
    public EntityExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
