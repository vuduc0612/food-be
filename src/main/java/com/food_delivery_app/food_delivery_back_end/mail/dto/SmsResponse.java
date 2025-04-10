package com.food_delivery_app.food_delivery_back_end.mail.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsResponse {
    private boolean success;
    private String message;
    private String toPhone;
    private String otp;
    private String rawResponse;
}


