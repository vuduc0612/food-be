package com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory;


import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserResponseDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByAccountId(Long accountId);

    @Query("""
            SELECT new com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponseDto(
            r.id, r.account.email, r.name, r.address, r.photoUrl, r.account.phoneNumber
            ) 
            FROM Restaurant r 
            JOIN r.account a 
            JOIN a.accountRoles ar 
            WHERE ar.isActive = true AND ar.roleType = "ROLE_RESTAURANT"
        """)
    Page<RestaurantResponseDto> findRestaurantsByActiveRole(Pageable pageable);

    @Query("""
            SELECT r FROM Restaurant r
            JOIN r.account a
            JOIN a.accountRoles ar
            WHERE ar.isActive = true AND ar.roleType = "ROLE_RESTAURANT"
            AND r.id = :id
    """)
    Optional<Restaurant> findRestaurantById(@Param("id") Long id);
}
