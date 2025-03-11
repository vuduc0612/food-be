package com.food_delivery_app.food_delivery_back_end.service;

import com.food_delivery_app.food_delivery_back_end.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.dto.RegisterUserDto;
import org.springframework.stereotype.Service;


public interface AuthService {
    String register(RegisterUserDto userAuthDto);
    String login(LoginDto loginDto);
}
