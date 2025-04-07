package com.food_delivery_app.food_delivery_back_end.modules.cart.dto;

import com.food_delivery_app.food_delivery_back_end.constant.AddToCartResultType;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponse;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private UserDto user;
    private RestaurantResponse restaurant;
    private List<CartItem> items;
    private Double totalAmount;
    private AddToCartResultType addToCartResultType;
}