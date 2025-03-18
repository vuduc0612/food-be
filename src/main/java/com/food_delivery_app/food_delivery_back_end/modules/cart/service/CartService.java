package com.food_delivery_app.food_delivery_back_end.modules.cart.service;

import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;

public interface CartService {
    Cart getCart(Long userId);
    void addToCart(Long userId, Long dishId, Integer quantity);
    void saveCart(Long userId, Cart cart);
    void updateItemQuantity(Long userId, Long dishId, Integer quantity);
    void removeItem(Long userId, Long dishId);
    void clearCart(Long userId);

}
