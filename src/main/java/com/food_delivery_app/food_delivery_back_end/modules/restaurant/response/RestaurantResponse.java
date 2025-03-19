package com.food_delivery_app.food_delivery_back_end.modules.restaurant.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String email;
}
