package com.fooddelivery.fooddeliveryapp.modules.cart.dto;

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

