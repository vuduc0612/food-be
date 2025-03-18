package com.food_delivery_app.food_delivery_back_end.modules.restaurant.service;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantService {
    List<RestaurantDto> getAllRestaurants();
    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);

}
