package com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.impl;

import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private ModelMapper modelMapper;
    private RestaurantRepository restaurantRepository;
    @Override
    public List<RestaurantDto> getAllRestaurants() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        return restaurantList.stream()
                .map((restaurant) -> modelMapper.map(restaurant, RestaurantDto.class))
                .collect(Collectors.toList());
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
