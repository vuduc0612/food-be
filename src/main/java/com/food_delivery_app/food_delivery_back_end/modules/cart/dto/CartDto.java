package com.food_delivery_app.food_delivery_back_end.modules.cart.dto;

import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {
    private Long userId;
    private Long restaurantId;
    private List<CartItem> items;
    private Double totalAmount;
}