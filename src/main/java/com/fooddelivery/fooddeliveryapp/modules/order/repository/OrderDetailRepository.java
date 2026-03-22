package com.fooddelivery.fooddeliveryapp.modules.order.repository;

import com.fooddelivery.fooddeliveryapp.modules.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
