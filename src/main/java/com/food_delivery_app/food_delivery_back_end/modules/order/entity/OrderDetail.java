package com.food_delivery_app.food_delivery_back_end.modules.order.entity;

import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_details")
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;
}
