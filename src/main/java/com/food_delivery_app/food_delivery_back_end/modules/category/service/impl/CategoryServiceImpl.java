package com.food_delivery_app.food_delivery_back_end.modules.category.service.impl;

import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.category.dto.CategoryDto;
import com.food_delivery_app.food_delivery_back_end.modules.category.entity.Category;
import com.food_delivery_app.food_delivery_back_end.modules.category.repository.CategoryRepository;
import com.food_delivery_app.food_delivery_back_end.modules.category.service.CategoryService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final RestaurantRepository restaurantRepository;
    private final AuthService authService;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Restaurant restaurant = authService.getCurrentRestaurant();
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setRestaurant(restaurant);
        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public Page<CategoryDto> getAllCategories(Long restaurantId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Page<CategoryDto> categoryDtos = categoryRepository.findByRestaurant(restaurant, pageable);
        return categoryDtos;
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        return null;
    }

    @Override
    public void deleteCategory(Long id) {

    }


}
