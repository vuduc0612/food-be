package com.fooddelivery.fooddeliveryapp.modules.cart.service;

import com.fooddelivery.fooddeliveryapp.modules.cart.dto.CartDto;
import com.fooddelivery.fooddeliveryapp.modules.cart.entity.Cart;

public interface CartService {
    CartDto getCart(Long userId);
    CartDto addToCart(Long userId, Long dishId, Integer quantity, boolean force);
    void saveCart(Long userId, Cart cart);
    void updateItemQuantity(Long userId, Long dishId, Integer quantity);
    void removeItem(Long userId, Long dishId);
    void clearCart(Long userId, Cart cart);
    void removeCart(Long userId);

}
