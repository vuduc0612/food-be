package com.fooddelivery.fooddeliveryapp.modules.user.entity;

import com.fooddelivery.fooddeliveryapp.modules.auth.entity.Account;
import com.fooddelivery.fooddeliveryapp.modules.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String username;

    private String address;

    private String photoUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();
}
