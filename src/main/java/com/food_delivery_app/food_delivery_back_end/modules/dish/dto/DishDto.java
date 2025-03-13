package com.food_delivery_app.food_delivery_back_end.modules.dish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {
    private String name;
    private Double price;
}
