package com.food_delivery_app.food_delivery_back_end.modules.cart.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food_delivery_app.food_delivery_back_end.constant.AddToCartResultType;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.cart.service.CartService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Optional;
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
//16727
    @Override
    public AddToCartResultType addToCart(Long userId, Long dishId, Integer quantity, boolean force) {
        Cart cart = getCart(userId);
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        Long newRestaurantId = dish.getRestaurant().getId();

        // Nếu giỏ không trống và khác nhà hàng
        if (!cart.getItems().isEmpty() && !cart.getRestaurantId().equals(newRestaurantId)) {
            if (!force) {
                return AddToCartResultType.CONFLICT_DIFFERENT_RESTAURANT;
            } else {
                clearCart(userId, cart); // xóa sạch món
//                cart.getItems().clear();
//                cart.setTotalAmount(0.0);
                System.out.println("Clear cart");
                cart.setRestaurantId(newRestaurantId);
            }
        }

        // Nếu giỏ hàng trống, thiết lập nhà hàng
        if (cart.getItems().isEmpty()) {
            cart.setRestaurantId(newRestaurantId);
        }

        // Cập nhật hoặc thêm món
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getIdDish().equals(dishId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setIdDish(dish.getId());
            cartItem.setQuantity(quantity);
            cartItem.setPrice(dish.getPrice());
            cartItem.setName(dish.getName());
            cartItem.setRestaurantId(newRestaurantId);
            cartItem.setThumbnail(dish.getThumbnail());

            cart.getItems().add(cartItem);
        }

        cart.updateTotalAmount();
        saveCart(userId, cart);

        return AddToCartResultType.SUCCESS;
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
                if(quantity == 0){
                    cart.getItems().remove(item);
                    break;
                }
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
    public void clearCart(Long userId, Cart cart) {
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
