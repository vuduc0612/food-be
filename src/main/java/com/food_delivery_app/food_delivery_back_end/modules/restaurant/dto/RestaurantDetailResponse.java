package com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto;

import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestaurantDetailResponse {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String photoUrl;
    private String phoneNumber;
    private List<DishDto> dishes;
}
