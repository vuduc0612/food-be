package com.fooddelivery.fooddeliveryapp.modules.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DeliveryFeeResponse {
    private double distanceKm;          // khoảng cách (km)
    private long durationSeconds;       // thời gian ước tính (giây)
    private int baseFee;                // phí cơ bản theo khoảng cách (VND)
    private double peakMultiplier;      // hệ số giờ cao điểm (1.0 - 1.8)
    private int finalFee;               // phí cuối cùng sau khi áp dụng tất cả (VND)
}