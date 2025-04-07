package com.food_delivery_app.food_delivery_back_end.modules.order.entity;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Entity
    @Table(name = "orders")
    @Builder
    public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Double totalAmount;
        private OrderStatusType status;
        @Column(name = "created_at")
        private LocalDateTime createdAt;
        //Địa chỉ giao hàng
        @Column(name = "delivery_address")
        private String deliveryAddress;
        //Phương thức thanh toán
        @Column(name = "payment_method")
        private String paymentMethod;
        //Trạng thái thanh toán
        @Column(name = "payment_status")
        private String paymentStatus;
        //Thời gian giao hàng
        @Column(name = "delivery_time")
        private LocalDateTime deliveryTime;
        //Ghi chú
        private String note;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne
        @JoinColumn(name = "restaurant_id", nullable = false)
        private Restaurant restaurant;

        @OneToMany(mappedBy = "order")
        private Set<OrderDetail> orderDetails = new HashSet<>();

    }
