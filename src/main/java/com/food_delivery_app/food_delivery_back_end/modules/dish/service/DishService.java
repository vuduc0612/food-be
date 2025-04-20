package com.food_delivery_app.food_delivery_back_end.modules.dish.service;

import com.food_delivery_app.food_delivery_back_end.constant.DishStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishResponseDto;

import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface DishService {
    Page<DishResponseDto> getAllDishes(int page, int limit);
    Page<DishResponseDto> getAllDishByRestaurant(Long restaurantId, Long categoryId, String keyword, int page, int limit);
    Page<DishResponseDto> getAllDishByCategory(Long categoryId, Long restaurantId, int page, int limit);
    DishResponseDto getDishById(Long id);
    DishResponseDto createDish(DishRequestDto dishResponseDto, Long restaurantId);
    DishResponseDto updateDish(Long id, Long restaurantId, DishRequestDto dishRequestDto);
    DishResponseDto updateStatusDish(Long id, Long restaurantId, DishStatusRequestDto status);
    void deleteDish(Long id, Long restaurantId);
}
