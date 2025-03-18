package com.food_delivery_app.food_delivery_back_end.modules.restaurant.controller;

import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private RestaurantService restaurantService;
    private AuthService authService;
    @GetMapping
    public List<RestaurantDto> getRestaurants() {
        return restaurantService.getAllRestaurants();

    }

    @PutMapping
    public RestaurantDto updateRestaurant(@RequestBody RestaurantDto restaurantDto) {
        Restaurant restaurant = authService.getCurrentRestaurant();
        return restaurantService.updateRestaurant(restaurant.getId(), restaurantDto);
    }


}
