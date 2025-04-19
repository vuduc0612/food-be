package com.food_delivery_app.food_delivery_back_end.modules.category.service;

import com.food_delivery_app.food_delivery_back_end.modules.category.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories(Long restaurantId);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
}
