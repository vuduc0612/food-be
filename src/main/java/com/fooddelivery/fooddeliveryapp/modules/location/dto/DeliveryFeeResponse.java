package com.fooddelivery.fooddeliveryapp.modules.location.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryFeeResponse {
    private double distanceKm;
    private long durationSeconds;
    private int fee; // đơn vị: VND
}