package com.food_delivery_app.food_delivery_back_end.modules.order.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.cart.service.CartService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.OrderDetail;
import com.food_delivery_app.food_delivery_back_end.modules.order.repository.OrderDetailRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.repository.OrderRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.response.OrderDetailResponse;
import com.food_delivery_app.food_delivery_back_end.modules.order.response.OrderResponse;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final Cart cart;
    private final AuthService authService;
    private final CartService cartService;

    /*
      -----CART-----
     */
    //add dish into the current user's cart
    @Override
    public void addToCart(Long dishId, Integer quantity) {
        cartService.addToCart(authService.getCurrentUser().getId(), dishId, quantity);
    }

    //get the current user's cart
    @Override
    public Cart getCart() {
        return cartService.getCart(authService.getCurrentUser().getId());
    }

    @Override
    public void clearCart() {
        cartService.clearCart(authService.getCurrentUser().getId());
    }

    @Override
    public void removeCart() {
        cartService.removeCart(authService.getCurrentUser().getId());
    }


    /*
        -----ORDER----
     */
    //place an order
    @Override
    @Transactional
    public OrderResponse placeOrder() {
        User user = authService.getCurrentUser();
        Long userId = user.getId();
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart");
        }

        Restaurant restaurant = restaurantRepository.findById(cart.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Order order = new Order();
        order.setUser(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatusType.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalAmount());
        Order savedOrder = orderRepository.save(order);


        for(CartItem cartItem : cart.getItems()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setDish(dishRepository.findById(cartItem.getIdDish())
                    .orElseThrow(() -> new RuntimeException("Dish not found"))
            );
            orderDetail.setOrder(savedOrder);
            savedOrder.getOrderDetails().add(orderDetail);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getPrice());
            orderDetailRepository.save(orderDetail);

        }
        OrderResponse orderResponse = modelMapper.map(savedOrder, OrderResponse.class);
        orderResponse.setOrderDetailResponses(
            savedOrder.getOrderDetails().stream()
                    .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                    .collect(Collectors.toList())
        );
        cartService.clearCart(userId);

        return orderResponse;
    }

    //Get all order of user
    @Override
    public Page<OrderResponse> getAllOrderOfUser(Long userId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Page<Order> orders = orderRepository.findByUser(user, pageable);

        Page<OrderResponse> orderResponses = orders.map(order -> {
                        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
                        orderResponse.setOrderDetailResponses(
                                order.getOrderDetails().stream()
                                        .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                                        .collect(Collectors.toList())
                        );
                        return orderResponse;
                });

        return orderResponses;
    }

    //Get alll order of restaurant
    @Override
    public Page<OrderResponse> getAllOrderOfRestaurant(Long restaurantId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Page<Order> orders = orderRepository.findByRestaurant(restaurant, pageable);

        Page<OrderResponse> orderResponses = orders.map(order -> {
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
            orderResponse.setOrderDetailResponses(
                    order.getOrderDetails().stream()
                            .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                            .collect(Collectors.toList())
            );
            return orderResponse;
        });

        return orderResponses;
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setOrderDetailResponses(
                order.getOrderDetails().stream()
                        .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                        .collect(Collectors.toList())
        );
        return orderResponse;

    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDto order) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setStatus(order.getStatus());
        return modelMapper.map(orderRepository.save(existingOrder), OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

}
