package com.food_delivery_app.food_delivery_back_end.modules.order.service;

import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.order.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    void addToCart(Long idDish, Integer quantity);
    OrderResponse placeOrder();
    List<OrderResponse> getAllOrder();
    Cart getCart();
    void clearCart();
    void removeCart();
}
