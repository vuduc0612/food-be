package com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @OneToOne()
    @JoinColumn(name = "account_id")
    private Account account;

    private String name;

    private String address;

    private String phone;

    private String description;

}
