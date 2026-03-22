package com.fooddelivery.fooddeliveryapp.modules.otp.repository;

import com.fooddelivery.fooddeliveryapp.modules.auth.entity.Account;
import com.fooddelivery.fooddeliveryapp.modules.otp.entity.OtpToken;
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
