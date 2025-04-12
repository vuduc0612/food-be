package com.food_delivery_app.food_delivery_back_end.modules.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@Builder
@Setter
@Getter
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}