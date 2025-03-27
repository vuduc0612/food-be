package com.food_delivery_app.food_delivery_back_end.modules.dish.service;

import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
@Service
public interface DishService {
    Page<DishDto> getAllDishes(int page, int limit);
    Page<DishDto> getAllDishByRestaurant(Long restaurantId, int page, int limit);
    Page<DishDto> getAllDishByCategory(Long categoryId, Long restaurantId, int page, int limit);
    DishDto getDishById(Long id);
    Optional<DishDto> getDish(Long id);
    DishDto createDish(DishDto dishDto, Long restaurantId);
    DishDto updateDish(Long id, DishDto dishDto);
    void deleteDish(Long id);
}
