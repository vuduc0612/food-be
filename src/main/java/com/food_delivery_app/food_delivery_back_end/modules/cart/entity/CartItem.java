package com.food_delivery_app.food_delivery_back_end.modules.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem implements Serializable {
    private Long dishId;
    private Integer quantity;
    private Double price;
    private String name;
    private String thumbnail;
}
