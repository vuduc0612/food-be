package com.fooddelivery.fooddeliveryapp.modules.location.service;

import com.fooddelivery.fooddeliveryapp.modules.location.dto.DeliveryFeeResponse;
import com.fooddelivery.fooddeliveryapp.modules.location.dto.DeliveryFeeRequest.Coordinate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

import java.time.LocalTime;

@Service
public class DeliveryFeeService {

    private final RestTemplate restTemplate = new RestTemplate();

    // === Cấu hình phí theo bậc thang (Tiered Pricing) ===
    private static final int BASE_FEE = 15000;             // phí cố định cho <= 3km
    private static final double TIER_1_LIMIT = 3.0;         // km
    private static final int TIER_1_RATE = 3000;            // VND/km (3-7km)
    private static final double TIER_2_LIMIT = 7.0;         // km
    private static final int TIER_2_RATE = 4000;            // VND/km (7-15km)
    private static final double TIER_3_LIMIT = 15.0;        // km
    private static final int TIER_3_RATE = 5000;            // VND/km (> 15km)
    private static final double MAX_DISTANCE = 30.0;        // km — từ chối giao nếu vượt quá


    /**
     * Tính phí giao hàng kết hợp 2 cơ chế:
     * 1. Tiered Pricing — phí theo bậc thang khoảng cách
     * 2. Dynamic Pricing — hệ số nhân theo giờ cao điểm
     *
     * Công thức: finalFee = baseFee × peakMultiplier
     */
    public DeliveryFeeResponse calculateFee(Coordinate from, Coordinate to) {
        // Gọi OSRM API để lấy khoảng cách và thời gian thực tế
        String osrmUrl = UriComponentsBuilder.fromHttpUrl("https://router.project-osrm.org/route/v1/driving/")
                .path(toLngLat(from) + ";" + toLngLat(to))
                .queryParam("overview", "false")
                .toUriString();

        String response = restTemplate.getForObject(osrmUrl, String.class);
        JSONObject json = new JSONObject(response);

        if (!json.has("routes") || json.getJSONArray("routes").isEmpty()) {
            throw new RuntimeException("Không thể tính được quãng đường");
        }

        JSONObject route = json.getJSONArray("routes").getJSONObject(0);
        double distanceMeters = route.getDouble("distance");
        long durationSeconds = route.getLong("duration");
        double distanceKm = distanceMeters / 1000.0;

        // Từ chối giao hàng nếu quá xa
//        if (distanceKm > MAX_DISTANCE) {
//            throw new RuntimeException("Khoảng cách quá xa (" + String.format("%.1f", distanceKm) + " km). Tối đa " + (int) MAX_DISTANCE + " km");
//        }

        // Tính phí theo bậc thang
        int baseFee = calculateTieredFee(distanceKm);

        // Áp dụng hệ số giờ cao điểm
        double peakMultiplier = getPeakMultiplier();
        int finalFee = (int) Math.ceil(baseFee * peakMultiplier);

        // Làm tròn lên bội số 1000₫
        finalFee = ((finalFee + 999) / 1000) * 1000;

        return DeliveryFeeResponse.builder()
                .distanceKm(Math.round(distanceKm * 10.0) / 10.0)
                .durationSeconds(durationSeconds)
                .baseFee(baseFee)
                .peakMultiplier(peakMultiplier)
                .finalFee(finalFee)
                .build();
    }

    /**
     * Tính phí theo bậc thang khoảng cách
     * 0 - 3 km  → 15.000₫
     * 3 - 7 km  → +3.000₫/km
     * 7 - 15 km → +4.000₫/km
     * > 15 km   → +5.000₫/km
     */
    private int calculateTieredFee(double distanceKm) {
        if (distanceKm <= TIER_1_LIMIT) {
            return BASE_FEE;
        }

        int fee = BASE_FEE;

        // Bậc 1: 3 - 7 km
        double tier1Distance = Math.min(distanceKm, TIER_2_LIMIT) - TIER_1_LIMIT;
        fee += (int) Math.ceil(tier1Distance * TIER_1_RATE);

        if (distanceKm <= TIER_2_LIMIT) return fee;

        // Bậc 2: 7 - 15 km
        double tier2Distance = Math.min(distanceKm, TIER_3_LIMIT) - TIER_2_LIMIT;
        fee += (int) Math.ceil(tier2Distance * TIER_2_RATE);

        if (distanceKm <= TIER_3_LIMIT) return fee;

        // Bậc 3: > 15 km
        double tier3Distance = distanceKm - TIER_3_LIMIT;
        fee += (int) Math.ceil(tier3Distance * TIER_3_RATE);

        return fee;
    }

    /**
     * Hệ số nhân theo giờ cao điểm
     * 11:00 - 13:00 → ×1.3 (giờ trưa)
     * 17:00 - 20:00 → ×1.5 (giờ tối)
     * 22:00 - 06:00 → ×1.8 (giờ khuya)
     * Còn lại       → ×1.0
     */
    double getPeakMultiplier() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour >= 11 && hour < 13) return 1.3;
        if (hour >= 17 && hour < 20) return 1.5;
        if (hour >= 22 || hour < 6) return 1.8;

        return 1.0;
    }



    private String toLngLat(Coordinate coord) {
        return coord.getLng() + "," + coord.getLat();
    }
}
