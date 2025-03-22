package com.food_delivery_app.food_delivery_back_end.modules.dish.service.impl;

import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.dish.service.DishService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DishServiceImpl implements DishService {
    private ModelMapper modelMapper;
    private DishRepository dishRepository;
    private RestaurantRepository restaurantRepository;
    @Override
    public Page<DishDto> getAllDishes(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Dish> dishPage = dishRepository.findAll(pageable);

        return dishPage.map(dish -> modelMapper.map(dish, DishDto.class));
    }

    @Override
    public DishDto getAllDishByRestaurantId(Long restaurantId) {
        Dish dish = dishRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        return modelMapper.map(dish, DishDto.class);
    }

    @Override
    public DishDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        return modelMapper.map(dish, DishDto.class);
    }

    @Override
    public Optional<DishDto> getDish(Long id) {
        return Optional.empty();
    }

    @Override
    public DishDto createDish(DishDto dishDto, Long restaurantId) {
        Dish dish = modelMapper.map(dishDto, Dish.class);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        dish.setRestaurant(restaurant);
        return modelMapper.map(dishRepository.save(dish), DishDto.class);
    }

    @Override
    public DishDto updateDish(Long id, DishDto dishDto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        dish.setName(dishDto.getName());
        dish.setPrice(dishDto.getPrice());
        return modelMapper.map(dishRepository.save(dish), DishDto.class);
    }

    @Override
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
