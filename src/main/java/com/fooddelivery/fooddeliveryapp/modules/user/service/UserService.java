package com.fooddelivery.fooddeliveryapp.modules.user.service;

import com.fooddelivery.fooddeliveryapp.modules.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserResponseDto> getAllUsers(int page, int limit);
    UserResponseDto updateUser(Long id, UserResponseDto userResponseDto);
    UserResponseDto getUser(Long id);
    void deleteUser(Long id);
}
