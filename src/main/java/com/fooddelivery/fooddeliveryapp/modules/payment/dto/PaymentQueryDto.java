package com.fooddelivery.fooddeliveryapp.modules.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaymentQueryDto {
    @JsonProperty("order_id")
    private String orderId; // Mã đơn hàng cần truy vấn

    @JsonProperty("trans_date")
    private String transDate; // Ngày giao dịch (định dạng yyyyMMddHHmmss)

    @JsonProperty("ip_address")
    private String ipAddress; // Địa chỉ IP của người dùng
}
