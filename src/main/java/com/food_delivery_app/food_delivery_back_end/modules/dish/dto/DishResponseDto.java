package com.food_delivery_app.food_delivery_back_end.modules.dish.dto;

import com.food_delivery_app.food_delivery_back_end.constant.DishStatusType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DishResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String thumbnail;
    private String category;
    private DishStatusType isAvailable;
    private Long restaurantId;
}
