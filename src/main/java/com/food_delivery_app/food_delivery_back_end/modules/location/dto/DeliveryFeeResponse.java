package com.food_delivery_app.food_delivery_back_end.modules.location.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryFeeResponse {
    private double distanceKm;
    private long durationSeconds;
    private int fee; // đơn vị: VND
}