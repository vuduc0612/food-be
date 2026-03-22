package com.fooddelivery.fooddeliveryapp.modules.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDto {
    private String name;
    private String address;
    private String phone;
    private String description;
    private Double latitude;
    private Double longitude;
    private String photoUrl;
}
