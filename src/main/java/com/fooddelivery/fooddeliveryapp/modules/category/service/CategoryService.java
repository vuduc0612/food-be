package com.fooddelivery.fooddeliveryapp.modules.category.service;

import com.fooddelivery.fooddeliveryapp.modules.category.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories(Long restaurantId);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
}
