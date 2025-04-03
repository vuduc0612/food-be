package com.food_delivery_app.food_delivery_back_end.modules.cart.dto;

import com.food_delivery_app.food_delivery_back_end.constant.AddToCartResultType;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long userId;
    private Long restaurantId;
    private List<CartItem> items;
    private Double totalAmount;
    private AddToCartResultType addToCartResultType;
}