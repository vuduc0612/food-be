package com.fooddelivery.fooddeliveryapp.modules.order.service.impl;

import com.fooddelivery.fooddeliveryapp.constant.OrderStatusType;
import com.fooddelivery.fooddeliveryapp.constant.PaymentMethodType;
import com.fooddelivery.fooddeliveryapp.modules.auth.service.AuthService;
import com.fooddelivery.fooddeliveryapp.modules.cart.service.CartService;
import com.fooddelivery.fooddeliveryapp.modules.dish.entity.Dish;
import com.fooddelivery.fooddeliveryapp.modules.dish.repository.DishRepository;
import com.fooddelivery.fooddeliveryapp.modules.cart.entity.Cart;
import com.fooddelivery.fooddeliveryapp.modules.cart.entity.CartItem;
import com.fooddelivery.fooddeliveryapp.modules.order.dto.OrderDetailResponse;
import com.fooddelivery.fooddeliveryapp.modules.order.dto.OrderRequestDto;
import com.fooddelivery.fooddeliveryapp.modules.order.dto.OrderResponseDto;
import com.fooddelivery.fooddeliveryapp.modules.order.dto.OrderUpdateRequestDto;
import com.fooddelivery.fooddeliveryapp.modules.order.entity.Order;
import com.fooddelivery.fooddeliveryapp.modules.order.entity.OrderDetail;
import com.fooddelivery.fooddeliveryapp.modules.order.repository.OrderDetailRepository;
import com.fooddelivery.fooddeliveryapp.modules.order.repository.OrderRepository;
import com.fooddelivery.fooddeliveryapp.modules.order.service.OrderService;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.repostitory.RestaurantRepository;
import com.fooddelivery.fooddeliveryapp.modules.user.dto.UserResponseDto;
import com.fooddelivery.fooddeliveryapp.modules.user.entity.User;
import com.fooddelivery.fooddeliveryapp.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final Cart cart;
    private final AuthService authService;
    private final CartService cartService;
    private final Executor orderTaskExecutor;

    public OrderServiceImpl(
            ModelMapper modelMapper,
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            DishRepository dishRepository,
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            Cart cart,
            AuthService authService,
            CartService cartService,
            @Qualifier("orderTaskExecutor") Executor orderTaskExecutor) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.cart = cart;
        this.authService = authService;
        this.cartService = cartService;
        this.orderTaskExecutor = orderTaskExecutor;
    }

    /*
        -----ORDER----
     */
    //place an order
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("[createOrder] Bắt đầu tạo đơn hàng - thread: {}", Thread.currentThread().getName());
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

        Order order = Order.builder()
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

        if ("CASH".equalsIgnoreCase(orderRequestDto.getPaymentMethod())) {
            order.setIsPaid(false);
        }

        Order savedOrder = orderRepository.save(order);

        // Song song: Lookup tất cả dish đồng thời bằng CompletableFuture
        List<CompletableFuture<OrderDetail>> futures = cart.getItems().stream()
                .map(cartItem -> CompletableFuture.supplyAsync(() -> {
                    log.info("[createOrder] Lookup dish {} - thread: {}", cartItem.getDishId(), Thread.currentThread().getName());
                    Dish dish = dishRepository.findById(cartItem.getDishId())
                            .orElseThrow(() -> new RuntimeException("Dish not found: " + cartItem.getDishId()));
                    return OrderDetail.builder()
                            .dish(dish)
                            .order(savedOrder)
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getPrice())
                            .build();
                }, orderTaskExecutor))
                .toList();

        // Chờ tất cả hoàn thành và thu thập kết quả
        List<OrderDetail> orderDetails = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        // Batch save: 1 query thay vì N queries
        orderDetailRepository.saveAll(orderDetails);
        savedOrder.getOrderDetails().addAll(orderDetails);
        log.info("[createOrder] Đã lưu {} order details", orderDetails.size());

        OrderResponseDto orderResponseDto = modelMapper.map(savedOrder, OrderResponseDto.class);
        orderResponseDto.setOrderDetailResponses(
            savedOrder.getOrderDetails().stream()
                    .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                    .collect(Collectors.toList())
        );

        // Async: Xóa giỏ hàng chạy nền, không block response
        if ("CASH".equalsIgnoreCase(orderRequestDto.getPaymentMethod())) {
            CompletableFuture.runAsync(() -> {
                log.info("[createOrder] Xóa giỏ hàng async - thread: {}", Thread.currentThread().getName());
                cartService.clearCart(userId, cart);
            }, orderTaskExecutor);
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
