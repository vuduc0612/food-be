package com.food_delivery_app.food_delivery_back_end.modules.order.dto;

import com.food_delivery_app.food_delivery_back_end.constant.OrderStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequestDto {
    private String deliceryAddress;
    private String status;
    private String note;
}
