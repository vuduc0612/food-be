package com.food_delivery_app.food_delivery_back_end.modules.auth.repository;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.AccountRole;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
