package com.food_delivery_app.food_delivery_back_end.modules.auth.service;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;


public interface AuthService {
    String register(RegisterDto userAuthDto, RoleType roleType);
    String login(LoginDto loginDto, RoleType roleType);

}
