package com.food_delivery_app.food_delivery_back_end.modules.user.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;
}
