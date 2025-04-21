package com.food_delivery_app.food_delivery_back_end.modules.statistics.controller;

import com.food_delivery_app.food_delivery_back_end.modules.statistics.dto.RevenueStatisticsResponse;
import com.food_delivery_app.food_delivery_back_end.modules.statistics.dto.StatisticPeriod;
import com.food_delivery_app.food_delivery_back_end.modules.statistics.service.RevenueStatisticsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("${api.prefix}/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics API", description = "Provides endpoints for statistics")
public class StatisticsController {

    private final RevenueStatisticsService revenueStatisticsService;

    
    /**
     * Endpoint lấy thống kê doanh thu theo thời gian
     *
     * @param restaurantId ID của nhà hàng
     * @param startDate Ngày bắt đầu (format: yyyy-MM-dd)
     * @param endDate Ngày kết thúc (format: yyyy-MM-dd)
     * @param period Khoảng thời gian (daily, weekly, monthly, yearly)
     * @return Thống kê doanh thu
     */
    @GetMapping("/restaurant/{restaurantId}/revenue")
    public ResponseEntity<RevenueStatisticsResponse> getRevenueStatistics(
            @PathVariable Long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "daily") String period) {
        System.out.println("Start date: " + startDate);
        System.out.println("End date: " + endDate);
        StatisticPeriod statisticPeriod = StatisticPeriod.fromString(period);
        
        RevenueStatisticsResponse response = revenueStatisticsService.getRevenueStatistics(
                restaurantId, startDate, endDate, statisticPeriod);
        
        return ResponseEntity.ok(response);
    }
} 