package com.food_delivery_app.food_delivery_back_end.modules.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequestDto {
    @NotBlank(message = "Delivery address cannot be blank")
    private String deliveryAddress;

    private String note;
    @NotBlank()
    private String paymentMethod;
    private Boolean isPaid;
    @NotNull()
    private Double totalAmount;

}
