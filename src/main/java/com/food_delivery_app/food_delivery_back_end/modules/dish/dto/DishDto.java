package com.food_delivery_app.food_delivery_back_end.modules.dish.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishDto {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String thumbnail;
    private String category;
    private Long restaurantId;
}
