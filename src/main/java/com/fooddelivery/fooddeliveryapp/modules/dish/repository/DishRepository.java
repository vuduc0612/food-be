package com.fooddelivery.fooddeliveryapp.modules.dish.repository;

import com.fooddelivery.fooddeliveryapp.modules.category.entity.Category;
import com.fooddelivery.fooddeliveryapp.modules.dish.entity.Dish;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    @Query(""" 
            SELECT d FROM Dish d WHERE d.restaurant = :restaurant
                        AND (:categoryId IS NULL OR d.category.id = :categoryId)
                        AND (:keyword IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Dish> findDishes(
            @Param("restaurant") Restaurant restaurant,
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Page<Dish> findByRestaurantAndCategory(Restaurant restaurant, Category category, Pageable pageable);
}
