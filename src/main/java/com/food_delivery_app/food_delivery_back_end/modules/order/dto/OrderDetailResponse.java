package com.food_delivery_app.food_delivery_back_end.modules.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private Long id;
    @JsonProperty("dish_name")
    private String dishName;
    private Integer quantity;
    @JsonProperty("dish_price")
    private Double totalPrice;
    private String thumbnail;
}
