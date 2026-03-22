package com.fooddelivery.fooddeliveryapp.modules.order.service;

import com.fooddelivery.fooddeliveryapp.constant.OrderStatusType;
import com.fooddelivery.fooddeliveryapp.modules.order.dto.OrderRequestDto;
import com.fooddelivery.fooddeliveryapp.modules.order.dto.OrderResponseDto;
import com.fooddelivery.fooddeliveryapp.modules.order.dto.OrderUpdateRequestDto;
import com.fooddelivery.fooddeliveryapp.modules.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
//    Page<OrderResponse> getAllOrder();
    Page<OrderResponseDto> getAllOrderOfUser(Long userId, OrderStatusType status, int page, int limit);
    Page<OrderResponseDto> getAllOrderOfRestaurant(Long restaurantId, OrderStatusType status, int page, int limit);
    OrderResponseDto getOrder(Long id);
    OrderResponseDto updateOrder(Long id, OrderUpdateRequestDto orderDto);
    List<UserResponseDto> getUserByRestaurant(Long restaurantId);
    void deleteOrder(Long id);
}
