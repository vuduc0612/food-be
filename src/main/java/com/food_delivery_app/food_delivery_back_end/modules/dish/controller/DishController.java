package com.food_delivery_app.food_delivery_back_end.modules.dish.controller;

import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.service.DishService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/dishes")
@Tag(name = "Dish API", description = "Provides endpoints for dish")
@RequiredArgsConstructor
public class DishController {
    private final AuthService authService;
    private final DishService dishService;
    //Get all dishes
    @GetMapping()
    public ResponseEntity<ResponseObject> getAllDishes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        Page<DishDto> dishDtos = dishService.getAllDishes(page, limit);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get all dishes successfully")
                        .data(dishDtos)
                        .status(HttpStatus.OK)
                        .build()
        );
    }
    //Create new dish
    @PostMapping()
    public ResponseEntity<ResponseObject> createDish(@Valid @RequestBody DishDto dishDto){
        Restaurant restaurant = authService.getCurrentRestaurant();
        //System.out.println(restaurant.getId());
        DishDto newDish = dishService.createDish(dishDto, restaurant.getId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(newDish)
                .message("Update dish successfully")
                .status(HttpStatus.CREATED)
                .build());
    }
    //Update dish
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateDish(
            @PathVariable Long id,
            @RequestBody DishDto dishDto ){
        DishDto updatedDishDto = dishService.updateDish(id, dishDto);
        return ResponseEntity.ok(ResponseObject.builder()
                        .data(updatedDishDto)
                        .message("Update dish successfully")
                        .status(HttpStatus.OK)
                        .build());
    }

}
