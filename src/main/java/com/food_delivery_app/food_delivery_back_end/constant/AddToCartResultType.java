package com.food_delivery_app.food_delivery_back_end.constant;

import lombok.Getter;

@Getter
public enum AddToCartResultType {
    SUCCESS,
    CONFLICT_DIFFERENT_RESTAURANT
}
