package com.fooddelivery.fooddeliveryapp.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends ApiException{
    public DataNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
