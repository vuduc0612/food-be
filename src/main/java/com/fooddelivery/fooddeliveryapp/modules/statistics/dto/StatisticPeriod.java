package com.fooddelivery.fooddeliveryapp.modules.statistics.dto;

import java.io.Serializable;

public enum StatisticPeriod implements Serializable {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;
    
    private static final long serialVersionUID = 1L;
    
    public static StatisticPeriod fromString(String value) {
        try {
            return StatisticPeriod.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return DAILY; // Giá trị mặc định
        }
    }
} 