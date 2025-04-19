package com.food_delivery_app.food_delivery_back_end.modules.dish.service.impl;

import com.food_delivery_app.food_delivery_back_end.modules.category.entity.Category;
import com.food_delivery_app.food_delivery_back_end.modules.category.repository.CategoryRepository;
import com.food_delivery_app.food_delivery_back_end.modules.category.service.CategoryService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
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
    public Page<DishDto> getAllDishes(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Dish> dishPage = dishRepository.findAll(pageable);
        return dishPage.map(dish -> modelMapper.map(dish, DishDto.class));
    }

    @Override
    public Page<DishDto> getAllDishByRestaurant(Long restaurantId, Long categoryId, String keyword, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Page<Dish> dishPage = dishRepository.findDishes(restaurant, categoryId, keyword, pageable);

        return dishPage.map(dish -> {
            DishDto dishDto = modelMapper.map(dish, DishDto.class);
            dishDto.setCategory(dish.getCategory().getName());
            return dishDto;

        });
    }

    @Override
    public Page<DishDto> getAllDishByCategory(Long categoryId, Long restaurantId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if(category.getRestaurant().getId() != restaurantId){
            throw new RuntimeException("Category not found in restaurant with id: " + restaurantId);
        }
        Page<Dish> dishPage = dishRepository.findByRestaurantAndCategory(restaurant, category, pageable);
        return dishPage.map(dish -> modelMapper.map(dish, DishDto.class));
    }


    @Override
    public DishDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        return modelMapper.map(dish, DishDto.class);
    }

    @Override
    public Optional<DishDto> getDish(Long id) {
        return Optional.empty();
    }

    @Override
    public DishDto createDish(DishDto dishDto, Long restaurantId) {
        Dish dish = Dish.builder()
                .name(dishDto.getName())
                .price(dishDto.getPrice())
                .description(dishDto.getDescription())
                .thumbnail(dishDto.getThumbnail())
                .build();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        dish.setRestaurant(restaurant);

        Category category;
        if(categoryRepository.existsByName(dishDto.getCategory())){
            category = categoryRepository.findByName(dishDto.getCategory())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } else {
            Category newCategory = Category.builder()
                    .name(dishDto.getCategory())
                    .build();
            newCategory.setRestaurant(restaurant);
            category = categoryRepository.save(newCategory);
        }
        dish.setCategory(category);

        return modelMapper.map(dishRepository.save(dish), DishDto.class);
    }

    @Override
    public DishDto updateDish(Long id, DishDto dishDto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        dish.setName(dishDto.getName());
        dish.setPrice(dishDto.getPrice());
        return modelMapper.map(dishRepository.save(dish), DishDto.class);
    }

    @Override
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
