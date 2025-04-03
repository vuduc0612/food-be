package com.food_delivery_app.food_delivery_back_end.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RegisterDto {
    @Email
    @NotNull
    private String email;

    private String fullName;
    private String phoneNumber;

    @NotNull
    private String password;
}
