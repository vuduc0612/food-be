package com.food_delivery_app.food_delivery_back_end.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class DataNotFoundException extends ApiException{
    public DataNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
