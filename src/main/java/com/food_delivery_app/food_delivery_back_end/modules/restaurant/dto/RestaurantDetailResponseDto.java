package com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto;

import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class RestaurantDetailResponseDto {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String photoUrl;
    private String phoneNumber;
    private Double longitude;
    private Double latitude;
    private List<DishDto> dishes;
}
