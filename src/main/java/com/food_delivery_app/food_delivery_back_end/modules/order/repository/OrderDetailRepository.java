package com.food_delivery_app.food_delivery_back_end.modules.order.repository;

import com.food_delivery_app.food_delivery_back_end.modules.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
