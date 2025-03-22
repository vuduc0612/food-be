package com.food_delivery_app.food_delivery_back_end.modules.restaurant.service;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.response.RestaurantResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantService {
    Page<RestaurantResponse> getAllRestaurants(int page, int limit);
    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);

}
