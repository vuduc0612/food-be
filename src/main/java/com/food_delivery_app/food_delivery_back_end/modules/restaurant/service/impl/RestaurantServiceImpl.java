package com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.impl;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.AccountRole;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.response.RestaurantResponse;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final AccountRepository accountRepository;

    @Override
    public Page<RestaurantResponse> getAllRestaurants(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<RestaurantResponse> restaurants = restaurantRepository.findAllRestaurantsWithNameAndPhoneAndAddress(pageable);
        return restaurants.map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class));
    }

    @Override
    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setName(restaurantDto.getName());
        restaurant.setPhone(restaurantDto.getPhone());
        restaurant.setAddress(restaurant.getAddress());
        restaurant.setDescription(restaurantDto.getDescription());
        restaurantRepository.save(restaurant);

        return modelMapper.map(restaurant, RestaurantDto.class);

    }

    @Override
    public RestaurantResponse getRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        return modelMapper.map(restaurant, RestaurantResponse.class);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Account account = accountRepository.findAccountByRestaurant(restaurant)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Set<AccountRole> accountRoles = account.getAccountRoles();
        for(AccountRole accountRole : accountRoles){
            if(accountRole.getRoleType().name().equals("ROLE_RESTAURANT")){
                System.out.println(accountRole.getRoleType().name());
                accountRole.setActive(false);
                break;
            }
        }
        account.setAccountRoles(accountRoles);
        accountRepository.save(account);
    }

}
