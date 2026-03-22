package com.fooddelivery.fooddeliveryapp.modules.cart.dto;

import com.fooddelivery.fooddeliveryapp.constant.AddToCartResultType;
import com.fooddelivery.fooddeliveryapp.modules.cart.entity.CartItem;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.dto.RestaurantResponseDto;
import com.fooddelivery.fooddeliveryapp.modules.user.dto.UserResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private UserResponseDto user;
    private RestaurantResponseDto restaurant;
    private List<CartItem> items;
    private Double totalAmount;
    private AddToCartResultType addToCartResultType;
}