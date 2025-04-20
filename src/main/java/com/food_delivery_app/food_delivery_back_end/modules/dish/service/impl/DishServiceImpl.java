package com.food_delivery_app.food_delivery_back_end.modules.dish.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.DishStatusType;
import com.food_delivery_app.food_delivery_back_end.exception.DataNotFoundException;
import com.food_delivery_app.food_delivery_back_end.modules.category.entity.Category;
import com.food_delivery_app.food_delivery_back_end.modules.category.repository.CategoryRepository;
import com.food_delivery_app.food_delivery_back_end.modules.category.service.CategoryService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishStatusRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.dish.service.DishService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final ModelMapper modelMapper;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @Override
    public Page<DishResponseDto> getAllDishes(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Dish> dishPage = dishRepository.findAll(pageable);
        dishPage.map(
                dish -> {
                   dish.setIsAvailable(DishStatusType.AVAILABLE);
                   dishRepository.save(dish);
                   return dish;
                }
        );
        return dishPage.map(dish -> modelMapper.map(dish, DishResponseDto.class));
    }

    @Override
    public Page<DishResponseDto> getAllDishByRestaurant(Long restaurantId, Long categoryId, String keyword, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Page<Dish> dishPage = dishRepository.findDishes(restaurant, categoryId, keyword, pageable);

        return dishPage.map(dish -> {
            DishResponseDto dishResponseDto = modelMapper.map(dish, DishResponseDto.class);
            dishResponseDto.setCategory(dish.getCategory().getName());
            return dishResponseDto;

        });
    }

    @Override
    public Page<DishResponseDto> getAllDishByCategory(Long categoryId, Long restaurantId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if(category.getRestaurant().getId() != restaurantId){
            throw new RuntimeException("Category not found in restaurant with id: " + restaurantId);
        }
        Page<Dish> dishPage = dishRepository.findByRestaurantAndCategory(restaurant, category, pageable);
        return dishPage.map(dish -> modelMapper.map(dish, DishResponseDto.class));
    }


    @Override
    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        return DishResponseDto.builder()
                .category(dish.getCategory().getName())
                .description(dish.getDescription())
                .id(dish.getId())
                .name(dish.getName())
                .thumbnail(dish.getThumbnail())
                .price(dish.getPrice())
                .build();
    }


    @Override
    public DishResponseDto createDish(DishRequestDto dishRequestDto, Long restaurantId) {
        Dish dish = Dish.builder()
                .name(dishRequestDto.getName())
                .price(dishRequestDto.getPrice())
                .description(dishRequestDto.getDescription())
                .thumbnail(dishRequestDto.getThumbnail())
                .build();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        dish.setRestaurant(restaurant);

        Category category;
        if(categoryRepository.existsByName(dishRequestDto.getCategoryName())){
            category = categoryRepository.findByName(dishRequestDto.getCategoryName())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } else {
            Category newCategory = Category.builder()
                    .name(dishRequestDto.getCategoryName())
                    .build();
            newCategory.setRestaurant(restaurant);
            category = categoryRepository.save(newCategory);
        }
        dish.setCategory(category);

        return modelMapper.map(dishRepository.save(dish), DishResponseDto.class);
    }

    @Override
    public DishResponseDto updateDish(Long id, Long restaurantId, DishRequestDto dishRequestDto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if(dish.getRestaurant().getId() != restaurantId){
            throw new DataNotFoundException("Dish not found in restaurant with id: " + restaurantId);
        }
        System.out.println(dishRequestDto.getPrice() + " " + dishRequestDto.getName() + " " + dishRequestDto.getDescription()
                + " " + dishRequestDto.getThumbnail() + " " + dishRequestDto.getCategoryName());
        dish.setName(dishRequestDto.getName());
        dish.setPrice(dishRequestDto.getPrice());
        dish.setDescription(dishRequestDto.getDescription());
        dish.setThumbnail(dishRequestDto.getThumbnail());

        Category category = categoryRepository.findByName(dishRequestDto.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        dish.setCategory(category);
        dishRepository.save(dish);
        return modelMapper.map(dishRepository.save(dish), DishResponseDto.class);
    }

    @Override
    public DishResponseDto updateStatusDish(Long id, Long restaurantId, DishStatusRequestDto dishStatus) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if(dish.getRestaurant().getId() != restaurantId){
            throw new DataNotFoundException("Dish not found in restaurant with id: " + restaurantId);
        }
        dish.setIsAvailable(DishStatusType.valueOf(dishStatus.getStatus().toUpperCase()));
        return modelMapper.map(dishRepository.save(dish), DishResponseDto.class);
    }


    @Override
    public void deleteDish(Long id, Long restaurantId) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if(dish.getRestaurant().getId() != restaurantId){
            throw new DataNotFoundException("Dish not found in restaurant with id: " + restaurantId);
        }
        dishRepository.delete(dish);
    }
}
