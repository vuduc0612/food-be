package com.food_delivery_app.food_delivery_back_end.modules.order.service;

import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.order.dto.OrderUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
//    Page<OrderResponse> getAllOrder();
    Page<OrderResponseDto> getAllOrderOfUser(Long userId, int page, int limit);
    Page<OrderResponseDto> getAllOrderOfRestaurant(Long restaurantId, int page, int limit);
    OrderResponseDto getOrder(Long id);
    OrderResponseDto updateOrder(Long id, OrderUpdateRequestDto orderDto);
    void deleteOrder(Long id);
}
