package com.food_delivery_app.food_delivery_back_end.modules.order.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.constant.PaymentMethodType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.cart.service.CartService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.Cart;
import com.food_delivery_app.food_delivery_back_end.modules.cart.entity.CartItem;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.*;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.OrderDetail;
import com.food_delivery_app.food_delivery_back_end.modules.order.repository.OrderDetailRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.repository.OrderRepository;
import com.food_delivery_app.food_delivery_back_end.modules.order.service.OrderService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
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
        -----ORDER----
     */
    //place an order
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        System.out.println("OrderRequestDto: " + orderRequestDto);
        User user = authService.getCurrentUser();
        Long userId = user.getId();
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = modelMapper.map(cartService.getCart(userId), Cart.class);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart");
        }

        Restaurant restaurant = restaurantRepository.findById(cart.getRestaurant().getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Order order =  Order.builder()
                .user(customer)
                .restaurant(restaurant)
                .status(OrderStatusType.PENDING)
                .createdAt(LocalDateTime.now())
                .totalAmount(orderRequestDto.getTotalAmount())
                .deliveryAddress(orderRequestDto.getDeliveryAddress())
                .paymentMethod(PaymentMethodType.valueOf(orderRequestDto.getPaymentMethod().toUpperCase()))
                .feeShipping(orderRequestDto.getFeeShipping())
                .discount(orderRequestDto.getDiscount())
                .note(orderRequestDto.getNote())
                .orderDetails(new HashSet<>())
                .build();


        if("CASH".equalsIgnoreCase(orderRequestDto.getPaymentMethod())) {
            order.setIsPaid(false);
        }

        Order savedOrder = orderRepository.save(order);

        for(CartItem cartItem : cart.getItems()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setDish(dishRepository.findById(cartItem.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found"))
            );
            orderDetail.setOrder(savedOrder);
            savedOrder.getOrderDetails().add(orderDetail);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getPrice());
            orderDetailRepository.save(orderDetail);

        }
        OrderResponseDto orderResponseDto = modelMapper.map(savedOrder, OrderResponseDto.class);
        orderResponseDto.setOrderDetailResponses(
            savedOrder.getOrderDetails().stream()
                    .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                    .collect(Collectors.toList())
        );
        if("CASH".equalsIgnoreCase(orderRequestDto.getPaymentMethod())) {
            cartService.clearCart(userId, cart);
        }
        return orderResponseDto;
    }

    //Get all order of user
    @Override
    public Page<OrderResponseDto> getAllOrderOfUser(Long userId,OrderStatusType status, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Page<Order> orders = orderRepository.findByUserAndStatus(user, pageable, status);

        Page<OrderResponseDto> orderResponses = orders.map(order -> {

                        OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
                        orderResponseDto.setOrderDetailResponses(
                                order.getOrderDetails().stream()
                                        .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                                        .collect(Collectors.toList())
                        );
                        return orderResponseDto;
                });

        return orderResponses;
    }

    //Get alll order of restaurant
    @Override
    public Page<OrderResponseDto> getAllOrderOfRestaurant(Long restaurantId, OrderStatusType status, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Page<Order> orders = orderRepository.findByRestaurantAndStatus(restaurant, pageable, status);
        if(status == null){
            orders = orderRepository.findByRestaurant(restaurant, pageable);
        }


        Page<OrderResponseDto> orderResponses = orders.map(order -> {
            OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .id(order.getUser().getId())
                    .name(order.getUser().getUsername())
                    .email(order.getUser().getAccount().getEmail())
                    .build();
            orderResponseDto.setUser(userResponseDto);
            orderResponseDto.setOrderDetailResponses(
                    order.getOrderDetails().stream()
                            .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                            .collect(Collectors.toList())
            );
            return orderResponseDto;
        });

        return orderResponses;
    }


    @Override
    public OrderResponseDto getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);

        orderResponseDto.setUser(UserResponseDto.builder()
                .name(order.getUser().getUsername())
                .email(order.getUser().getAccount().getEmail())
                .phoneNumber(order.getUser().getAccount().getPhoneNumber())
                .address(order.getUser().getAddress())
                .build());
        orderResponseDto.setOrderDetailResponses(
                order.getOrderDetails().stream()
                        .map(orderDetail -> {
                            return OrderDetailResponse.builder()
                                    .id(orderDetail.getDish().getId())
                                    .dishName(orderDetail.getDish().getName())
                                    .quantity(orderDetail.getQuantity())
                                    .totalPrice(orderDetail.getPrice() * orderDetail.getQuantity())
                                    .thumbnail(orderDetail.getDish().getThumbnail())
                                    .build();
                        })
                        .collect(Collectors.toList())
        );
        return orderResponseDto;

    }

    @Override
    public List<UserResponseDto> getUserByRestaurant(Long restaurantId){

        return null;
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, OrderUpdateRequestDto orderDto) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setStatus(OrderStatusType.valueOf(orderDto.getStatus().toUpperCase()));
        return modelMapper.map(orderRepository.save(existingOrder), OrderResponseDto.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

}
