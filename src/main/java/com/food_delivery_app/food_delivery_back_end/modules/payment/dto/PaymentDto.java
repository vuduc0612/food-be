package com.food_delivery_app.food_delivery_back_end.modules.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private Long amount;
    @JsonProperty("bank_code")
    private String bankCode;
    @JsonProperty("order_id")
    private Long orderId;
}
