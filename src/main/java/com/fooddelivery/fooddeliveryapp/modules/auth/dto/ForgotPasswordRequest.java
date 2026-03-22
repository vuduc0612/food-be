package com.fooddelivery.fooddeliveryapp.modules.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}