package com.food_delivery_app.food_delivery_back_end.modules.auth.service;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterResponse;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;


public interface AuthService {
    RegisterResponse register(RegisterDto userAuthDto, RoleType roleType);
    boolean verifyOtp(RegisterDto userAuthDto, String otp, RoleType roleType);

    String login(LoginDto loginDto, RoleType roleType);
    User getCurrentUser();
    Restaurant getCurrentRestaurant();
}
