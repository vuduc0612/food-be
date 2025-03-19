package com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.impl;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.response.RestaurantResponse;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    @Override
    public List<RestaurantResponse> getAllRestaurants(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<RestaurantResponse> restaurantList = restaurantRepository.findAllRestaurantsWithNameAndPhoneAndAddress(pageable);
        return restaurantList;
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


}
