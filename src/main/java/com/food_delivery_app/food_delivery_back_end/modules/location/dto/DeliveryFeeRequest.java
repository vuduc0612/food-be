package com.food_delivery_app.food_delivery_back_end.modules.location.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DeliveryFeeRequest {
    private Coordinate from;
    private Coordinate to;

    @Data
    public static class Coordinate {
        private double lat;
        private double lng;
    }
}