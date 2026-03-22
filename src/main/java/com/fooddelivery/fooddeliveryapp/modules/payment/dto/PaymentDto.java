package com.fooddelivery.fooddeliveryapp.modules.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PaymentDto {
    private Long amount;
    @JsonProperty("bank_code")
    private String bankCode;
    @JsonProperty("order_id")
    private Long orderId;
}
