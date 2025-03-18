package com.food_delivery_app.food_delivery_back_end.modules.order.service;

import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    void addToCart(Long idDish, Integer quantity);
    Order placeOrder(Long userId);
    Cart getCart();
    Cart getCartForUser(Long userId);
}
