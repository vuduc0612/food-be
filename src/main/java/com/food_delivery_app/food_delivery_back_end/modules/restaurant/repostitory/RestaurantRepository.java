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
            SELECT DISTINCT new com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponseDto(
               r.id, r.account.email, r.name, r.address, r.photoUrl, r.account.phoneNumber, r.longitude, r.latitude
            )
            FROM Restaurant r 
            JOIN r.account a 
            JOIN a.accountRoles ar 
            JOIN r.dishes d 
            WHERE ar.isActive = true 
            AND ar.roleType = 'ROLE_RESTAURANT'
            AND (
                LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                OR LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """)
    Page<RestaurantResponseDto> findRestaurantsByActiveRole(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT r FROM Restaurant r
            JOIN r.account a
            JOIN a.accountRoles ar
            WHERE ar.isActive = true AND ar.roleType = "ROLE_RESTAURANT"
            AND r.id = :id
    """)
    Optional<Restaurant> findRestaurantById(@Param("id") Long id);
}
