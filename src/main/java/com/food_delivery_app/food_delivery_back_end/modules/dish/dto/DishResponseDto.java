package com.food_delivery_app.food_delivery_back_end.modules.dish.dto;

import com.food_delivery_app.food_delivery_back_end.constant.DishStatusType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DishResponseDto {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String thumbnail;
    private String category;
    private DishStatusType isAvailable;
    private Long restaurantId;
}
