package com.fooddelivery.fooddeliveryapp.modules.otp.entity;

import com.fooddelivery.fooddeliveryapp.modules.auth.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_token")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false)
    private boolean verified;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
