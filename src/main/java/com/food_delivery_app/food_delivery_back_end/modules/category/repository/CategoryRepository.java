package com.food_delivery_app.food_delivery_back_end.modules.category.repository;

import com.food_delivery_app.food_delivery_back_end.modules.category.dto.CategoryDto;
import com.food_delivery_app.food_delivery_back_end.modules.category.entity.Category;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Page<CategoryDto> findByRestaurant(Restaurant restaurant, Pageable pageable);
    boolean existsByName(String name);
}
