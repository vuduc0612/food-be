package com.fooddelivery.fooddeliveryapp.modules.order.dto;

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
