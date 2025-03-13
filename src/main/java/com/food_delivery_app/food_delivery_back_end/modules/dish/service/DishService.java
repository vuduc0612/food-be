package com.food_delivery_app.food_delivery_back_end.modules.dish.service;

import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;

import java.util.List;
import java.util.Optional;

public interface DishService {
    List<DishDto> getAllDishes();
    Optional<DishDto> getDish(Long id);
    DishDto createDish(DishDto dishDto);
    DishDto updateDish(Long id, DishDto dishDto);
    void deleteDish(Long id);
}
