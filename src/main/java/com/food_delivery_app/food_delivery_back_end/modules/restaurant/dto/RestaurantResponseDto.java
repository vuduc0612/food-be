package com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestaurantResponseDto {
    private Long id;
    private String email;
    private String name;
    private String address;
    private String photoUrl;
    private String phoneNumber;
    private Double longitude;
    private Double latitude;
}
