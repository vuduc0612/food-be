package com.food_delivery_app.food_delivery_back_end.modules.statistics.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.statistics.dto.RevenueStatisticsResponse;
import com.food_delivery_app.food_delivery_back_end.modules.statistics.dto.StatisticPeriod;
import com.food_delivery_app.food_delivery_back_end.modules.statistics.repository.OrderStatisticsRepository;
import com.food_delivery_app.food_delivery_back_end.modules.statistics.service.RevenueStatisticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RevenueStatisticsServiceImpl implements RevenueStatisticsService, Serializable {
    private static final long serialVersionUID = 1L;

    private final OrderStatisticsRepository orderStatisticsRepository;

    
    @Override
    @Cacheable(value = "revenueStatistics", key = "{#restaurantId, #startDate, #endDate, #period}")
    public RevenueStatisticsResponse getRevenueStatistics(Long restaurantId, LocalDate startDate, LocalDate endDate, StatisticPeriod period) {
        try {
            log.info("Getting revenue statistics for restaurant {}, from {} to {}, period {}", 
                    restaurantId, startDate, endDate, period);
            
            // Chuyển đổi từ LocalDate sang LocalDateTime để truy vấn
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            
            // Chỉ xem xét các đơn hàng đã hoàn thành
            OrderStatusType status = OrderStatusType.DELIVERED;
            
            // Tính tổng doanh thu
            Double totalRevenue = orderStatisticsRepository.getTotalRevenueByRestaurantAndDateRange(
                    restaurantId, startDateTime, endDateTime, status);
            totalRevenue = totalRevenue != null ? totalRevenue : 0.0;
            
            // Tính giá trị trung bình đơn hàng
            Double averageOrderValue = orderStatisticsRepository.getAverageOrderValueByRestaurantAndDateRange(
                    restaurantId, startDateTime, endDateTime, status);
            averageOrderValue = averageOrderValue != null ? averageOrderValue : 0.0;
            
            // Tính khoảng thời gian trước đó với cùng độ dài
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            LocalDateTime previousStartDateTime = startDateTime.minusDays(daysBetween);
            LocalDateTime previousEndDateTime = startDateTime.minusSeconds(1);
            
            // Lấy doanh thu cho khoảng thời gian trước đó
            Double previousRevenue = orderStatisticsRepository.getTotalRevenueByRestaurantAndDateRange(
                    restaurantId, previousStartDateTime, previousEndDateTime, status);
            previousRevenue = previousRevenue != null ? previousRevenue : 0.0;
            
            // Tính phần trăm thay đổi
            Double comparisonWithPreviousPeriod = 0.0;
            if (previousRevenue > 0) {
                comparisonWithPreviousPeriod = ((totalRevenue - previousRevenue) / previousRevenue) * 100;
            }
            
            // Lấy doanh thu theo khoảng thời gian được chỉ định
            List<RevenueStatisticsResponse.RevenuePeriodDTO> revenueByPeriod = getRevenueByPeriod(
                    restaurantId, startDateTime, endDateTime, status, period);
            
            // Tạo response
            return RevenueStatisticsResponse.builder()
                    .totalRevenue(totalRevenue)
                    .averageOrderValue(averageOrderValue)
                    .comparisonWithPreviousPeriod(comparisonWithPreviousPeriod)
                    .revenueByPeriod(revenueByPeriod)
                    .build();
        } catch (Exception e) {
            log.error("Error getting revenue statistics", e);
            throw e;
        }
    }
    
    /**
     * Lấy doanh thu theo khoảng thời gian
     *
     * @param restaurantId ID của nhà hàng
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @param status Trạng thái đơn hàng cần xét (thường là COMPLETED)
     * @param period Khoảng thời gian (DAILY, WEEKLY, MONTHLY, YEARLY)
     * @return Danh sách doanh thu theo khoảng thời gian
     */
    private List<RevenueStatisticsResponse.RevenuePeriodDTO> getRevenueByPeriod(
            Long restaurantId, LocalDateTime startDate, LocalDateTime endDate, 
            OrderStatusType status, StatisticPeriod period) {
        
        List<Object[]> revenueData;
        
        // Lấy dữ liệu theo khoảng thời gian
        switch (period) {
            case DAILY:
                revenueData = orderStatisticsRepository.getDailyRevenueByRestaurantAndDateRange(
                        restaurantId, startDate, endDate, status);
                break;
            case WEEKLY:
                revenueData = orderStatisticsRepository.getWeeklyRevenueByRestaurantAndDateRange(
                        restaurantId, startDate, endDate, status);
                break;
            case MONTHLY:
                revenueData = orderStatisticsRepository.getMonthlyRevenueByRestaurantAndDateRange(
                        restaurantId, startDate, endDate, status);
                break;
            case YEARLY:
                revenueData = orderStatisticsRepository.getYearlyRevenueByRestaurantAndDateRange(
                        restaurantId, startDate, endDate, status);
                break;
            default:
                revenueData = orderStatisticsRepository.getDailyRevenueByRestaurantAndDateRange(
                        restaurantId, startDate, endDate, status);
        }
        
        // Chuyển đổi kết quả thành DTO
        List<RevenueStatisticsResponse.RevenuePeriodDTO> result = new ArrayList<>();
        for (Object[] data : revenueData) {
            String periodString = data[0].toString();
            Double revenue = (Double) data[1];
            
            result.add(new RevenueStatisticsResponse.RevenuePeriodDTO(periodString, revenue));
        }
        
        return result;
    }
} 