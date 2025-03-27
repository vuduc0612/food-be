package com.food_delivery_app.food_delivery_back_end.modules.auth.entity;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "account")
    private Set<AccountRole> accountRoles = new HashSet<>();

    @OneToOne(mappedBy = "account")
    private User user;

    @OneToOne(mappedBy = "account")
    private Restaurant restaurant;
}
