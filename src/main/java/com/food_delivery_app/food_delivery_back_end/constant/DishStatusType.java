package com.food_delivery_app.food_delivery_back_end.constant;

public enum DishStatusType {
    AVAILABLE("available"),
    UNAVAILABLE("unavailable"),
    DELETE("delete");;
    private final String status;

    DishStatusType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
