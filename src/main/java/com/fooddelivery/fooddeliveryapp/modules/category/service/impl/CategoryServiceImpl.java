package com.fooddelivery.fooddeliveryapp.modules.category.service.impl;

import com.fooddelivery.fooddeliveryapp.modules.auth.service.AuthService;
import com.fooddelivery.fooddeliveryapp.modules.category.dto.CategoryDto;
import com.fooddelivery.fooddeliveryapp.modules.category.entity.Category;
import com.fooddelivery.fooddeliveryapp.modules.category.repository.CategoryRepository;
import com.fooddelivery.fooddeliveryapp.modules.category.service.CategoryService;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.repostitory.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<CategoryDto> getAllCategories(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        List<Category> categories = categoryRepository.findByRestaurant(restaurant);
        List<CategoryDto> categoryDtos = categories.stream()
                .map(category ->{
                    return CategoryDto.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .build();
                })
                .toList();
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
