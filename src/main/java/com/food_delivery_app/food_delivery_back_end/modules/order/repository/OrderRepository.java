package com.food_delivery_app.food_delivery_back_end.modules.order.repository;

import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order>  findByRestaurant(Restaurant restaurant);
    Page<Order> findByUser(User user, Pageable pageable);
    Page<Order> findByRestaurant(Restaurant restaurant, Pageable pageable);

}
