package com.food_delivery_app.food_delivery_back_end.modules.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {
    private Long dishId;
    private Integer quantity;
    private Boolean force;
}

