package com.food_delivery_app.food_delivery_back_end.modules.order.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailResponse {
    private Long id;
    private String dishName;
    private Integer quantity;
    private Double price;
}
