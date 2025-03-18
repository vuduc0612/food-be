package com.food_delivery_app.food_delivery_back_end.modules.order.repository;

import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
