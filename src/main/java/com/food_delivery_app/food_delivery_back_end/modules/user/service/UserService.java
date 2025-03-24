package com.food_delivery_app.food_delivery_back_end.modules.user.service;

import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<UserDto> getAllUsers(int page, int limit);
    UserDto updateUser(Long id, UserDto userDto);
    UserDto getUser(Long id);
    void deleteUser(Long id);
}
