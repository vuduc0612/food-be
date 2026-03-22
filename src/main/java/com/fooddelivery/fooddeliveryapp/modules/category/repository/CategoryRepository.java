package com.fooddelivery.fooddeliveryapp.modules.category.repository;

import com.fooddelivery.fooddeliveryapp.modules.category.entity.Category;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByRestaurant(Restaurant restaurant);
    boolean existsByName(String name);
}
