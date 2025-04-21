package com.food_delivery_app.food_delivery_back_end.modules.statistics.repository;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import com.food_delivery_app.food_delivery_back_end.modules.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderStatisticsRepository extends JpaRepository<Order, Long> {

    // Tổng doanh thu của nhà hàng trong khoảng thời gian
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status = :status")
    Double getTotalRevenueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatusType status);

    // Đếm số đơn hàng của nhà hàng trong khoảng thời gian
    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status = :status")
    Long countOrdersByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatusType status);

    // Giá trị trung bình đơn hàng
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status = :status")
    Double getAverageOrderValueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatusType status);

    // Doanh thu theo ngày
    @Query("SELECT FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m-%d') as day, SUM(o.totalAmount) as revenue " +
           "FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status = :status " +
           "GROUP BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m-%d') " +
           "ORDER BY day")
    List<Object[]> getDailyRevenueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatusType status);

    // Doanh thu theo tuần
    @Query("SELECT CONCAT(FUNCTION('YEAR', o.createdAt), '-W', FUNCTION('WEEK', o.createdAt)) as weekYear, " +
           "SUM(o.totalAmount) as revenue " +
           "FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status = :status " +
           "GROUP BY FUNCTION('YEAR', o.createdAt), FUNCTION('WEEK', o.createdAt) " +
           "ORDER BY FUNCTION('YEAR', o.createdAt), FUNCTION('WEEK', o.createdAt)")
    List<Object[]> getWeeklyRevenueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatusType status);

    // Doanh thu theo tháng
    @Query("SELECT CONCAT(FUNCTION('YEAR', o.createdAt), '-', " +
           "FUNCTION('LPAD', FUNCTION('MONTH', o.createdAt), 2, '0')) as yearMonth, " +
           "SUM(o.totalAmount) as revenue " +
           "FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status = :status " +
           "GROUP BY FUNCTION('YEAR', o.createdAt), FUNCTION('MONTH', o.createdAt) " +
           "ORDER BY FUNCTION('YEAR', o.createdAt), FUNCTION('MONTH', o.createdAt)")
    List<Object[]> getMonthlyRevenueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatusType status);

    // Doanh thu theo năm
    @Query("SELECT FUNCTION('YEAR', o.createdAt) as year, " +
           "SUM(o.totalAmount) as revenue " +
           "FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status = :status " +
           "GROUP BY FUNCTION('YEAR', o.createdAt) " +
           "ORDER BY year")
    List<Object[]> getYearlyRevenueByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatusType status);

    // Tìm tất cả đơn hàng của nhà hàng trong khoảng thời gian
    List<Order> findByRestaurantIdAndCreatedAtBetweenAndStatus(
            Long restaurantId, 
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            OrderStatusType status);
} 