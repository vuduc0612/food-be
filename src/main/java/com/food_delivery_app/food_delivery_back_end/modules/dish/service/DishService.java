package com.food_delivery_app.food_delivery_back_end.modules.dish.service;

import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface DishService {
    List<DishDto> getAllDishes();
    DishDto getAllDishByRestaurantId(Long restaurantId);
    DishDto getDishById(Long id);
    Optional<DishDto> getDish(Long id);
    DishDto createDish(DishDto dishDto, Long restaurantId);
    DishDto updateDish(Long id, DishDto dishDto);
    void deleteDish(Long id);
}
