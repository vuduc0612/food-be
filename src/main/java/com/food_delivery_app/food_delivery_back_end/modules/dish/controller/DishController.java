package com.food_delivery_app.food_delivery_back_end.modules.dish.controller;

import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.service.DishService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/dish")
@Tag(name = "Dish API", description = "Provides endpoints for dish")
@AllArgsConstructor
public class DishController {
    private AuthService authService;
    private DishService dishService;
    @GetMapping()
    public List<DishDto> getAllDishes(){
        return dishService.getAllDishes();
    }
    @PostMapping()
    public DishDto addDish(@RequestBody DishDto dishDto){
        Restaurant restaurant = authService.getCurrentRestaurant();
        System.out.println(restaurant.getId());
        return dishService.createDish(dishDto, restaurant.getId());
    }

}
