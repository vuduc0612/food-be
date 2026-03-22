package com.fooddelivery.fooddeliveryapp.modules.order.repository;

import com.fooddelivery.fooddeliveryapp.constant.OrderStatusType;
import com.fooddelivery.fooddeliveryapp.modules.order.entity.Order;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
import com.fooddelivery.fooddeliveryapp.modules.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order>  findByRestaurant(Restaurant restaurant);
    Page<Order> findByUserAndStatus(User user, Pageable pageable, OrderStatusType status);
    Page<Order> findByRestaurantAndStatus(Restaurant restaurant, Pageable pageable, OrderStatusType status);
    Page<Order> findByRestaurant(Restaurant restaurant, Pageable pageable);
    @Query("SELECT o.user FROM Order o WHERE o.restaurant.id = :restaurantId")
    Set<User> findUsersByRestaurantId(@Param("restaurantId") Long restaurantId);

}
