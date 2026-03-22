package com.fooddelivery.fooddeliveryapp.modules.order.dto;

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
    private Double totalPrice;
    private String thumbnail;
}
