package com.food_delivery_app.food_delivery_back_end.modules.auth.repository;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

}
