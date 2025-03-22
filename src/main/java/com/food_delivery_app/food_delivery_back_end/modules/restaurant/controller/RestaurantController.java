package com.food_delivery_app.food_delivery_back_end.modules.restaurant.controller;

import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.response.RestaurantResponse;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.RestaurantService;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private RestaurantService restaurantService;
    private AuthService authService;

    //Get all restaurants
    @GetMapping("")
    public ResponseEntity<ResponseObject> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<RestaurantResponse> restaurants = restaurantService.getAllRestaurants(page, limit);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get restaurants successfully")
                        .data(restaurants)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //Update restaurant
    @PutMapping("")
    public ResponseEntity<ResponseObject> updateRestaurant(@RequestBody RestaurantDto restaurantDto) {
        Restaurant restaurant = authService.getCurrentRestaurant();

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .data(restaurant)
                        .message("Update restaurant successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

}
