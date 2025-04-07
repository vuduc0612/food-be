package com.food_delivery_app.food_delivery_back_end.modules.cart.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food_delivery_app.food_delivery_back_end.constant.AddToCartResultType;
import com.food_delivery_app.food_delivery_back_end.modules.cart.dto.CartDto;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.cart.service.CartService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponse;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    private String getCartKey(Long userId){
        return CART_PREFIX + userId;
    }

    @Override
    public CartDto getCart(Long userId) {
        User us = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto user = UserDto.builder()
                .id(us.getId())
                .name(us.getUsername())
                .email(us.getAccount().getEmail())
                .phoneNumber(us.getAccount().getPhoneNumber())
                .address(us.getAddress())
                .build();
        //System.out.println(user.getId() + " " + user.getName() + " " + user.getEmail() + " " + user.getPhoneNumber() +  " " + user.getAddress());
        String cartKey = getCartKey(userId);
        Object obj = redisTemplate.opsForValue().get(cartKey);

        if (obj == null) {
            Cart emptyCart = new Cart();
            emptyCart.setUser(user);
            return modelMapper.map(emptyCart, CartDto.class);
        }

        Cart cart;
        if (obj instanceof Cart) {
            cart = (Cart) obj;
        } else {
            cart = objectMapper.convertValue(obj, Cart.class);
        }
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto addToCart(Long userId, Long dishId, Integer quantity, boolean force) {
        Cart cart = modelMapper.map(getCart(userId), Cart.class);

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        RestaurantResponse newRestaurant = modelMapper.map(dish.getRestaurant(), RestaurantResponse.class);

        // Nếu giỏ không trống và khác nhà hàng
        if (!cart.getItems().isEmpty() && !cart.getRestaurant().getId().equals(newRestaurant.getId())) {
            if (!force) {
                CartDto cartDto = modelMapper.map(cart, CartDto.class);
                cartDto.setAddToCartResultType(AddToCartResultType.CONFLICT_DIFFERENT_RESTAURANT);
                return cartDto;
            } else {
                clearCart(userId, cart); // xóa sạch món
                cart.setRestaurant(newRestaurant);
            }
        }

        // Nếu giỏ hàng trống, thiết lập nhà hàng
        if (cart.getItems().isEmpty()) {
            cart.setRestaurant(newRestaurant);
        }

        // Cập nhật hoặc thêm món
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getDishId().equals(dishId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setDishId(dish.getId());
            cartItem.setQuantity(quantity);
            cartItem.setPrice(dish.getPrice());
            cartItem.setName(dish.getName());
//            cartItem.setRestaurant(newRestaurant);
            cartItem.setThumbnail(dish.getThumbnail());

            cart.getItems().add(cartItem);
        }

        cart.updateTotalAmount();
        saveCart(userId, cart);

        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        cartDto.setAddToCartResultType(AddToCartResultType.SUCCESS);
        return cartDto;
    }


    @Override
    public void saveCart(Long userId, Cart cart) {
        String cartKey = getCartKey(userId);
        redisTemplate.opsForValue().set(cartKey, cart);
        redisTemplate.expire(cartKey, 1, TimeUnit.DAYS);
    }

    @Override
    public void updateItemQuantity(Long userId, Long dishId, Integer quantity) {
        Cart cart = modelMapper.map(getCart(userId), Cart.class);
        for(CartItem item : cart.getItems()){
            if(item.getDishId().equals(dishId)){
                if(quantity == 0){
                    cart.getItems().remove(item);
                    cart.setRestaurant(null);
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
        Cart cart = modelMapper.map(getCart(userId), Cart.class);
        cart.getItems().removeIf(item -> item.getDishId().equals(dishId));
        cart.updateTotalAmount();
        saveCart(userId, cart);
    }

    @Override
    public void clearCart(Long userId, Cart cart) {
        cart.setRestaurant(null);
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
