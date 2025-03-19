package com.food_delivery_app.food_delivery_back_end.modules.cart.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.cart.service.CartService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCartService implements CartService {
    private static final String CART_PREFIX = "cart:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final DishRepository dishRepository;

    private String getCartKey(Long userId){
        return CART_PREFIX + userId;
    }

    @Override
    public Cart getCart(Long userId) {
        String cartKey = getCartKey(userId);
        Object obj = redisTemplate.opsForValue().get(cartKey);

        if (obj instanceof LinkedHashMap<?,?>) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(obj, Cart.class);
        }

        if(obj == null){
            Cart cart = new Cart();
            cart.setUserId(userId);

            return cart;
        }
        return (Cart) obj;
    }

    @Override
    public void addToCart(Long userId, Long dishId, Integer quantity) {
        Cart cart = getCart(userId);
        Dish dish = dishRepository.findById(dishId).
                orElseThrow(() -> new RuntimeException("Dish not found"));
        CartItem cartItem = new CartItem();
        cartItem.setIdDish(dish.getId());
        cartItem.setQuantity(quantity);
        cartItem.setPrice(dish.getPrice());
        cartItem.setName(dish.getName());
        cartItem.setRestaurantId(dish.getRestaurant().getId());

        if(cart.getItems().isEmpty()) {
            cart.setRestaurantId(dish.getRestaurant().getId());

        } else if(!cart.getRestaurantId().equals(dish.getRestaurant().getId())) {
            throw new IllegalArgumentException("Cart can only contain items from one restaurant");
        }

        boolean itemExists = false;
        for(CartItem existingItem : cart.getItems()){
            if(existingItem.getIdDish().equals(dishId)){
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }
        if(!itemExists){
            cart.getItems().add(cartItem);
        }
        cart.updateTotalAmount();
        saveCart(userId, cart);

    }

    @Override
    public void saveCart(Long userId, Cart cart) {
        String cartKey = getCartKey(userId);
        redisTemplate.opsForValue().set(cartKey, cart);
        redisTemplate.expire(cartKey, 1, TimeUnit.DAYS);
    }

    @Override
    public void updateItemQuantity(Long userId, Long dishId, Integer quantity) {
        Cart cart = getCart(userId);
        for(CartItem item : cart.getItems()){
            if(item.getIdDish().equals(dishId)){
                item.setQuantity(quantity);

                break;
            }
        }
        cart.updateTotalAmount();
        saveCart(userId, cart);
    }

    @Override
    public void removeItem(Long userId, Long dishId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getIdDish().equals(dishId));
        cart.updateTotalAmount();
        saveCart(userId, cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.setRestaurantId(null);
        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        saveCart(userId, cart);
    }

    @Override
    public void removeCart(Long userId) {
        String cartKey = getCartKey(userId);
        redisTemplate.delete(cartKey);
    }
}
