package com.fooddelivery.fooddeliveryapp.modules.restaurant.service;

import com.fooddelivery.fooddeliveryapp.modules.restaurant.dto.RestaurantDto;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.dto.RestaurantDetailResponseDto;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.dto.RestaurantResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface RestaurantService {
    Page<RestaurantResponseDto> getAllRestaurants(String keyword, int page, int limit);
    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);
    RestaurantDetailResponseDto getRestaurant(Long id);
//    RestaurantResponse getRestaurant(Long id);
    void deleteRestaurant(Long id);
    String uploadImage(Long id, MultipartFile file) throws IOException;
}
