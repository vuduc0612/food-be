package com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
