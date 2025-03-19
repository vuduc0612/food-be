package com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory;


import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.response.RestaurantResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByAccountId(Long accountId);
    @Query("SELECT new com.food_delivery_app.food_delivery_back_end.modules.restaurant.response.RestaurantResponse(r.id, r.name, r.address, r.account.email) FROM Restaurant r")
    List<RestaurantResponse> findAllRestaurantsWithNameAndPhoneAndAddress(Pageable pageable);
}
