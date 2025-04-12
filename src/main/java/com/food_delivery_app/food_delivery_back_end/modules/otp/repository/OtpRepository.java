package com.food_delivery_app.food_delivery_back_end.modules.otp.repository;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.otp.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface OtpRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByAccountAndVerifiedFalseAndExpiryTimeAfter(Account account, LocalDateTime now);
    boolean existsByAccountAndVerifiedTrue(Account account);
    void deleteByAccountAndVerifiedFalse(Account account);
}
