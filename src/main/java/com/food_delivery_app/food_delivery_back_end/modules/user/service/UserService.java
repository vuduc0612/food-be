package com.food_delivery_app.food_delivery_back_end.modules.user.service;

import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
}
