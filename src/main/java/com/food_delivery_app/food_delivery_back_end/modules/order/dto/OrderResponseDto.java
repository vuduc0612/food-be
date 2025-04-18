package com.food_delivery_app.food_delivery_back_end.modules.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponseDto {
    @JsonProperty("id")
    private Long orderId;
    private UserResponseDto user;
    private RestaurantResponseDto restaurant;
    @JsonProperty("restaurant_name")
    private String restaurantName;
    @JsonProperty("total_price")
    private Double totalAmount;
    @JsonProperty("shipping_fee")
    private Double feeShipping;
    private OrderStatusType status;
    @JsonProperty("paid")
    private boolean isPaid;
    @JsonProperty("delivery_address")
    private String deliveryAddress;
    @JsonProperty("order_time")
    private String createdAt;
    @JsonProperty("items")
    private List<OrderDetailResponse> orderDetailResponses;

}
