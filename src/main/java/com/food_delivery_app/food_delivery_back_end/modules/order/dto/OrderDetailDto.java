package com.food_delivery_app.food_delivery_back_end.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailDto {
    private Long orderId;
    private Long dishId;
    private Integer quantity;

}
