package com.food_delivery_app.food_delivery_back_end.modules.restaurant.service;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDetailResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface RestaurantService {
    Page<RestaurantResponseDto> getAllRestaurants(int page, int limit);
    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);
    RestaurantDetailResponseDto getRestaurant(Long id);
//    RestaurantResponse getRestaurant(Long id);
    void deleteRestaurant(Long id);
    String uploadImage(Long id, MultipartFile file) throws IOException;
}
