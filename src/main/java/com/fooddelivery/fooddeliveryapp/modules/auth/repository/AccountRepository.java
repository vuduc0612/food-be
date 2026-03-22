package com.fooddelivery.fooddeliveryapp.modules.auth.repository;

import com.fooddelivery.fooddeliveryapp.modules.auth.entity.Account;
import com.fooddelivery.fooddeliveryapp.modules.auth.entity.AccountRole;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
import com.fooddelivery.fooddeliveryapp.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Account> findByPhoneNumber(String phoneNumber);
    Optional<Account> findAccountByUser(User user);
    Optional<Account> findAccountByUserAndAccountRolesAndStatusTrue(User user, Set<AccountRole> accountRoles);
    Optional<Account> findAccountByRestaurant(Restaurant restaurant);




}
