package com.food_delivery_app.food_delivery_back_end.modules.order.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.OrderDetail;
import com.food_delivery_app.food_delivery_back_end.modules.order.repository.OrderDetailRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.repository.OrderRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final Cart cart;
    private final AuthService authService;
    private final Map<Long, Cart> userCarts = new ConcurrentHashMap<>();

    @Override
    public void addToCart(Long idDish, Integer quantity) {
        System.out.println("SidDish: " + idDish + " Squantity: " + quantity);
        Long userId = authService.getCurrentUser().getId();

        Cart cart = getCartForUser(userId);
        Dish dish = dishRepository.findById(idDish)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found"));
        CartItem cartItem = new CartItem();
        cartItem.setIdDish(dish.getId());
        cartItem.setQuantity(quantity);
        cartItem.setPrice(dish.getPrice());
        cartItem.setName(dish.getName());
        cartItem.setRestaurantId(dish.getRestaurant().getId());
        cart.addItem(cartItem);
        for (CartItem item : cart.getItems()) {
            System.out.println("Dish: " + item.getName() + " Quantity: " + item.getQuantity());
        }
    }

    @Override
    public Cart getCart() {
        Long userId = authService.getCurrentUser().getId();

        return getCartForUser(userId);
    }

    @Override
    public Cart getCartForUser(Long userId) {
        return userCarts.computeIfAbsent(userId, id -> {
            Cart newCart = new Cart();
            newCart.setUserId(id);
            return newCart;
        });
    }

    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(cart.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Order order = new Order();
        order.setUser(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatusType.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalAmount());
        Order savedOrder = orderRepository.save(order);
        List<OrderDetail> orderDetails = cart.getItems().stream()
                .map(cartItem -> {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setDish(dishRepository.findById(cartItem.getIdDish())
                            .orElseThrow(() -> new RuntimeException("Dish not found"))
                    );
                    orderDetail.setOrder(savedOrder);
                    orderDetail.setQuantity(cartItem.getQuantity());
                    return orderDetail;

                })
                .collect(Collectors.toList());
        orderDetailRepository.saveAll(orderDetails);
        return savedOrder;
    }
}
