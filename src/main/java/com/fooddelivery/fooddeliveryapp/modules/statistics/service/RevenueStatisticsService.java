package com.fooddelivery.fooddeliveryapp.modules.statistics.service;

import com.fooddelivery.fooddeliveryapp.modules.statistics.dto.RevenueStatisticsResponse;
import com.fooddelivery.fooddeliveryapp.modules.statistics.dto.StatisticPeriod;

import java.time.LocalDate;

public interface RevenueStatisticsService {
    
    /**
     * Lấy thống kê doanh thu cho nhà hàng trong khoảng thời gian
     *
     * @param restaurantId ID của nhà hàng
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @param period Khoảng thời gian (DAILY, WEEKLY, MONTHLY, YEARLY)
     * @return Thống kê doanh thu
     */
    RevenueStatisticsResponse getRevenueStatistics(Long restaurantId, 
                                                 LocalDate startDate, 
                                                 LocalDate endDate, 
                                                 StatisticPeriod period);
} 