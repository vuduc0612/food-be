package com.food_delivery_app.food_delivery_back_end.modules.dish.repository;

import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {

}
