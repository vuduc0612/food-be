package com.fooddelivery.fooddeliveryapp.modules.dish.dto;

import com.fooddelivery.fooddeliveryapp.constant.DishStatusType;
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
