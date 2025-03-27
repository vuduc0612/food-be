package com.food_delivery_app.food_delivery_back_end.modules.auth.entity;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private boolean isActive;
}