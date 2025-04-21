package com.food_delivery_app.food_delivery_back_end.modules.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatisticsResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Double totalRevenue;
    private Double averageOrderValue;
    private Double comparisonWithPreviousPeriod;
    private List<RevenuePeriodDTO> revenueByPeriod;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenuePeriodDTO implements Serializable {
        private static final long serialVersionUID = 2L;
        private String period;
        private Double revenue;
    }
} 