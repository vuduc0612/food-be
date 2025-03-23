package com.food_delivery_app.food_delivery_back_end.modules.user.controller;

import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.service.UserService;
import com.food_delivery_app.food_delivery_back_end.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@AllArgsConstructor
public class UserController {
    private final ModelMapper modelMapper;
    private UserService userService;
    private AuthService authService;

    @GetMapping()
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers(10);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserDto getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return modelMapper.map(authService.getCurrentUser(), UserDto.class);
    }
}
