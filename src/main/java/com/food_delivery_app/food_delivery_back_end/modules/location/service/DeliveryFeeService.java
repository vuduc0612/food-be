package com.food_delivery_app.food_delivery_back_end.modules.location.service;




import com.food_delivery_app.food_delivery_back_end.modules.location.dto.DeliveryFeeResponse;
import com.food_delivery_app.food_delivery_back_end.modules.location.dto.DeliveryFeeRequest.Coordinate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

@Service
public class DeliveryFeeService {

    private final RestTemplate restTemplate = new RestTemplate();

    public DeliveryFeeResponse calculateFee(Coordinate from, Coordinate to) {
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
        int fee = calculateFeeFromDistance(distanceKm);

        return new DeliveryFeeResponse(distanceKm, durationSeconds, fee);
    }

    private String toLngLat(Coordinate coord) {
        return coord.getLng() + "," + coord.getLat();
    }

    private int calculateFeeFromDistance(double distanceKm) {
        int baseFee = 15000; // dưới 3km
        if (distanceKm <= 3) return baseFee;
        return baseFee + (int) Math.ceil((distanceKm - 3) * 3000); // mỗi km thêm 3000đ
    }
}
