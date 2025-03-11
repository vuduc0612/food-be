package com.food_delivery_app.food_delivery_back_end.controller;

import com.food_delivery_app.food_delivery_back_end.dto.UserDto;
import com.food_delivery_app.food_delivery_back_end.entity.User;
import com.food_delivery_app.food_delivery_back_end.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    @GetMapping()
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
}
