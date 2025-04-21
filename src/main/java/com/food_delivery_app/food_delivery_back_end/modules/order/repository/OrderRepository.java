package com.food_delivery_app.food_delivery_back_end.modules.order.repository;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order>  findByRestaurant(Restaurant restaurant);
    Page<Order> findByUserAndStatus(User user, Pageable pageable, OrderStatusType status);
    Page<Order> findByRestaurantAndStatus(Restaurant restaurant, Pageable pageable, OrderStatusType status);
    Page<Order> findByRestaurant(Restaurant restaurant, Pageable pageable);
    @Query("SELECT o.user FROM Order o WHERE o.restaurant.id = :restaurantId")
    Set<User> findUsersByRestaurantId(@Param("restaurantId") Long restaurantId);

}
