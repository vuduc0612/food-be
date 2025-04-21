package com.food_delivery_app.food_delivery_back_end.constant;

import java.io.Serializable;

public enum DishStatusType implements Serializable {
    AVAILABLE("available"),
    UNAVAILABLE("unavailable"),
    DELETE("delete");
    
    private static final long serialVersionUID = 1L;
    private final String status;

    DishStatusType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
