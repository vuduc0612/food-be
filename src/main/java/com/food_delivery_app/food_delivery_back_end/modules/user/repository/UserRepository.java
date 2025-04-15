package com.food_delivery_app.food_delivery_back_end.modules.user.repository;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(Long accountId);

    @Query("""
            SELECT new com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserResponseDto(
            u.id, u.account.email,u.username, u.address, u.account.phoneNumber
            ) 
            FROM User u 
            JOIN u.account a 
            JOIN a.accountRoles ar 
            WHERE ar.isActive = true AND ar.roleType = :roleType 
        """)
    Page<UserResponseDto> findUsersByActiveRole(@Param("roleType") RoleType roleType, Pageable pageable);

    @Query("""  
            SELECT new com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserResponseDto(
            u.id, u.account.email,u.username, u.address, u.account.phoneNumber
            ) 
            FROM User u 
            JOIN u.account a 
            JOIN a.accountRoles ar 
            WHERE ar.isActive = true AND ar.roleType = "ROLE_USER"
            AND u.id = :id
    """)
    Optional<UserResponseDto> findUserById(@Param("id") Long id);
    @Query("""
            SELECT u
            FROM User u
            JOIN u.orders o
            WHERE o.id = :orderId
    """)
    Optional<User> findUserByOrderId(Long orderId);

}
