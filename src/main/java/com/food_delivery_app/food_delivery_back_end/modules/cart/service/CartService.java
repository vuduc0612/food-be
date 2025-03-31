package com.food_delivery_app.food_delivery_back_end.modules.cart.service;

import com.food_delivery_app.food_delivery_back_end.constant.AddToCartResultType;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;

public interface CartService {
    Cart getCart(Long userId);
    AddToCartResultType addToCart(Long userId, Long dishId, Integer quantity, boolean force);
    void saveCart(Long userId, Cart cart);
    void updateItemQuantity(Long userId, Long dishId, Integer quantity);
    void removeItem(Long userId, Long dishId);
    void clearCart(Long userId, Cart cart);
    void removeCart(Long userId);

}
