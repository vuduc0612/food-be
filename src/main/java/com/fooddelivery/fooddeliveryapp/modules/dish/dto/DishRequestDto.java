package com.fooddelivery.fooddeliveryapp.modules.dish.dto;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDto {
    private String name;
    private Double price;
    private String thumbnail;
    private String categoryName;
    private String description;
}
