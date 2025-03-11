package com.food_delivery_app.food_delivery_back_end.service;

import com.food_delivery_app.food_delivery_back_end.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
}
