package com.food_delivery_app.food_delivery_back_end.modules.dish.repository;

import com.food_delivery_app.food_delivery_back_end.modules.category.entity.Category;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Page<Dish> findByRestaurant(Restaurant restaurant, Pageable pageable);
    Page<Dish> findByRestaurantAndCategory(Restaurant restaurant, Category category, Pageable pageable);
}
