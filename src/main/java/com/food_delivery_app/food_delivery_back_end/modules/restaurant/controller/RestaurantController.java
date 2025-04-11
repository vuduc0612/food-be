package com.food_delivery_app.food_delivery_back_end.modules.restaurant.controller;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDetailResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.RestaurantService;
import com.food_delivery_app.food_delivery_back_end.response.CustomPageResponse;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private RestaurantService restaurantService;
    private AuthService authService;

    //Fake data
    @GetMapping("/fake-data")
    public ResponseEntity<ResponseObject> fakeData() {
        Faker faker = new Faker(new Locale("vi"));
        for (int i = 0; i < 10; i++) {
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().cellPhone();
            String name = faker.company().name();
            RegisterDto registerDto = new RegisterDto(email, phone, name, "password123");
            authService.register(registerDto, RoleType.ROLE_RESTAURANT);

            List<Restaurant> restaurants = restaurantRepository.findAll();
            for(Restaurant restaurant : restaurants){
                restaurant.setName(faker.company().name());
                restaurant.setAddress(faker.address().fullAddress());
                restaurantRepository.save(restaurant);
            }
        }

        System.out.println("Generated 1000 fake restaurants successfully!");
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Generated 1000 fake restaurants successfully!")
                        .status(HttpStatus.OK)
                        .build()
        );
    }


    //Get all restaurants
    @GetMapping("")
    public ResponseEntity<CustomPageResponse<RestaurantResponseDto>> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants(page, limit);
        return ResponseEntity.ok(
                CustomPageResponse.<RestaurantResponseDto>builder()
                        .message("Get all restaurants successfully")
                        .status(HttpStatus.OK)
                        .data(restaurants.getContent())
                        .currentPage(restaurants.getNumber())
                        .totalItems(restaurants.getTotalElements())
                        .totalPages(restaurants.getTotalPages())
                        .build()
        );
    }

    //Get restaurant by id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getRestaurant(@PathVariable Long id) {
        RestaurantDetailResponseDto restaurant = restaurantService.getRestaurant(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .data(restaurant)
                        .message("Get restaurant successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //Upload restaurant image
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ResponseObject> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String url = restaurantService.uploadImage(id, file);
        System.out.println("URL: " + url);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .data(url)
                        .message("Upload image successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }
    //Update restaurant
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateRestaurant(
            @PathVariable Long id,
            @RequestBody RestaurantDto restaurantDto) {
        RestaurantDto restaurant = restaurantService.updateRestaurant(id, restaurantDto);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .data(restaurant)
                        .message("Update restaurant successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //Delete restaurant
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete restaurant successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

}
