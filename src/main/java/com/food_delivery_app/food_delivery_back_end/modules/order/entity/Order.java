package com.food_delivery_app.food_delivery_back_end.modules.order.entity;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.constant.PaymentMethodType;
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

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "fee_shipping")
    private Double feeShipping;

    private Double discount;

    private OrderStatusType status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "payment_method")
    private PaymentMethodType paymentMethod;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    @Column(name = "transaction_id")
    private String transactionId;

    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

}
