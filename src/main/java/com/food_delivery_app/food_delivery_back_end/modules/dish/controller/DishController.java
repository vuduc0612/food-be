package com.food_delivery_app.food_delivery_back_end.modules.dish.controller;

import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.dish.service.DishService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.response.CustomPageResponse;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import com.github.javafaker.Faker;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/dishes")
@Tag(name = "Dish API", description = "Provides endpoints for dish")
@RequiredArgsConstructor
public class DishController {
    private final AuthService authService;
    private final DishService dishService;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    //Fake data
    @GetMapping("/fake-data")
    public ResponseEntity<ResponseObject> fakeData() {
        Faker faker = new Faker(new Locale("vi"));
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for(Restaurant restaurant: restaurants){
            Long idRestaurant = restaurant.getId();
            for(int i = 0; i < 10; i++){
                String dishName = faker.food().ingredient();
                String dishDescription = faker.lorem().sentence();
                Double dishPrice = faker.number().randomDouble(2, 1, 100);
                Dish dish = new Dish();
                dish.setName(dishName);
                dish.setDescription(dishDescription);
                dish.setPrice(dishPrice);
                dish.setRestaurant(restaurant);
                dishRepository.save(dish);

            }
            System.out.println("Generated 10 fake dishes successfully!" + idRestaurant);
        }

        return ResponseEntity.ok(ResponseObject.builder()
                .message("Generated 1000 fake dishes successfully!")
                .status(HttpStatus.OK)
                .build());
    }

    //Get all dishes
    @GetMapping()
    public ResponseEntity<CustomPageResponse<DishDto>> getAllDishes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<DishDto> dishDtos = dishService.getAllDishes(page, limit);
        return ResponseEntity.ok(
                CustomPageResponse.<DishDto>builder()
                        .message("Get all dishes successfully")
                        .status(HttpStatus.OK)
                        .data(dishDtos.getContent())
                        .currentPage(dishDtos.getNumber())
                        .totalItems(dishDtos.getTotalElements())
                        .totalPages(dishDtos.getTotalPages())
                        .build()
        );
    }

    //Create new dish
    @PostMapping()
    public ResponseEntity<ResponseObject> createDish(@Valid @RequestBody DishDto dishDto) {
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
            @RequestBody DishDto dishDto) {
        DishDto updatedDishDto = dishService.updateDish(id, dishDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedDishDto)
                .message("Update dish successfully")
                .status(HttpStatus.OK)
                .build());
    }

}
