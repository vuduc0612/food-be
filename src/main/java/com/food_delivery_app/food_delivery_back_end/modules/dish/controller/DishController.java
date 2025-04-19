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
import com.food_delivery_app.food_delivery_back_end.utils.FileUtils;
import com.food_delivery_app.food_delivery_back_end.utils.UploadUtils;
import com.github.javafaker.Faker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/dishes")
@Tag(name = "Dish API", description = "Provides endpoints for dish")
@RequiredArgsConstructor
public class DishController {
    private static final Logger logger = LoggerFactory.getLogger(DishController.class);
    private final AuthService authService;
    private final DishService dishService;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final UploadUtils uploadUtils;

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
    @Operation(summary = "Get all dishes", description = "Returns all dishes")
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
    //Get dish by id restaurant
    @GetMapping("/restaurant/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    @Operation(summary = "Get all dishes by restaurant", description = "Returns all dishes by restaurant")
    public ResponseEntity<CustomPageResponse<DishDto>> getDishesByRestaurant(
            @PathVariable Long id,
            @RequestParam(defaultValue = "null") Long categoryId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<DishDto> dishDtos = dishService.getAllDishByRestaurant(id,categoryId, keyword, page, limit);
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

    @GetMapping("/restaurant/me")
    @PreAuthorize("hasRole('RESTAURANT')")
    @Operation(summary = "Get all dishes by restaurant", description = "Returns all dishes by restaurant")
    public ResponseEntity<CustomPageResponse<DishDto>> getDishesByRestaurantCurrent(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Long id = authService.getCurrentRestaurant().getId();
        System.out.println("===> categoryId: " + categoryId);
        System.out.println("===> keyword: " + keyword);
        Page<DishDto> dishDtos = dishService.getAllDishByRestaurant(id,categoryId, keyword, page, limit);
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

    //Get dishes by category and restaurant
    @GetMapping("/category/{categoryId}/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('RESTAURANT')")
    @Operation(summary = "Get all dishes by category and restaurant", description = "Returns all dishes by category and restaurant")
    public ResponseEntity<CustomPageResponse<DishDto>> getDishesByCategoryAndRestaurant(
            @PathVariable Long categoryId,
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<DishDto> dishDtos = dishService.getAllDishByCategory(categoryId, restaurantId, page, limit);
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    public ResponseEntity<ResponseObject> createDish(@Valid @ModelAttribute DishDto dishDto,
                                                     @RequestPart("file") MultipartFile file) throws Exception {
        if(file == null){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("File is required")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        if(file.getSize() > 10 * 1024 * 1024){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("File size is too large")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
//
//        String fileName = FileUtils.storeFile(file);
//        System.out.println(fileName);
//        dishDto.setThumbnail(fileName);

        String thumbnail = uploadUtils.uploadFile(file);
        dishDto.setThumbnail(thumbnail);
        Restaurant restaurant = authService.getCurrentRestaurant();

        DishDto newDish = dishService.createDish(dishDto, dishDto.getRestaurantId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(newDish)
                .message("Update dish successfully")
                .status(HttpStatus.CREATED)
                .build());
    }

    //Get image
    @GetMapping("/images/{imageName}")
    @Operation(summary = "Get image of dish", description = "Returns image of dish")
    public ResponseEntity<?> getImage(@PathVariable String imageName){
        try{
            Path imagePath = Path.of(FileUtils.UPLOAD_DIR, imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                logger.info(imageName + " not found");
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving image: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //Update dish
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @Operation(summary = "Update dish", description = "Returns updated dish")
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    @Operation(summary = "Delete dish", description = "Returns deleted dish")
    public ResponseEntity<ResponseObject> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Delete dish successfully")
                .status(HttpStatus.OK)
                .build());
    }
}
