package com.fooddelivery.fooddeliveryapp.modules.cart.entity;

import com.fooddelivery.fooddeliveryapp.modules.restaurant.dto.RestaurantResponseDto;
import com.fooddelivery.fooddeliveryapp.modules.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype")
@Getter
@Setter
public class Cart {
    private UserResponseDto user;
    private RestaurantResponseDto restaurant;
    private List<CartItem> items = new ArrayList<>();
    private Double totalAmount = 0.0;

    public void updateTotalAmount(){
        totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
