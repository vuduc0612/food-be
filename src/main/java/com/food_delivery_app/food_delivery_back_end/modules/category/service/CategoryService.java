package com.food_delivery_app.food_delivery_back_end.modules.category.service;

import com.food_delivery_app.food_delivery_back_end.modules.category.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    Page<CategoryDto> getAllCategories(Long restaurantId, int page, int limit);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
}
