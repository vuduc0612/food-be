package com.food_delivery_app.food_delivery_back_end.modules.user.entity;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String username;

    private String address;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new HashSet<>();
}
