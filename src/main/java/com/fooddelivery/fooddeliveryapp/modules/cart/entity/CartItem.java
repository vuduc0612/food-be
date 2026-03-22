package com.fooddelivery.fooddeliveryapp.modules.cart.entity;

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
