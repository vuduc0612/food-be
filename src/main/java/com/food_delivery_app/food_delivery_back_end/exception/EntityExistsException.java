package com.food_delivery_app.food_delivery_back_end.exception;

import org.springframework.http.HttpStatus;

public class EntityExistsException extends ApiException {
    public EntityExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
