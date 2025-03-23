package com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String description;

    @OneToMany(mappedBy = "restaurant")
    private Set<Dish> dishes = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    private Set<Order> orders = new HashSet<>();
}
