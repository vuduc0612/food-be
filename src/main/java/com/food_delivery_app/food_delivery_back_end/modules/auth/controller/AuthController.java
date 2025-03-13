package com.food_delivery_app.food_delivery_back_end.modules.auth.controller;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/auth")
@Tag(name = "Users API", description = "Provides endpoints for customer")
public class AuthController {
    @Autowired
    private AuthService authService;
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!!";
    }

    @PostMapping("/customer/register")
    @Operation(summary = "Register a new customer", description = "Returns the status registered customer")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerUserDto){
        String response = authService.register(registerUserDto, RoleType.ROLE_USER);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("customer/login")
    @Operation(summary = "Login a user", description = "Return token to authenticate user")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto){
        String response = authService.login(loginDto, RoleType.ROLE_USER);
        System.out.println("Token: " + response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/restaurant/register")
    @Operation(summary = "Register a new restaurant", description = "Returns the status registered restaurant")
    public ResponseEntity<String> registerRestaurant(@RequestBody RegisterDto registerUserDto){
        String response = authService.register(registerUserDto, RoleType.ROLE_RESTAURANT);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("restaurant/login")
    @Operation(summary = "Login a user", description = "Return token to authenticate user")
    public ResponseEntity<String> loginRestaurant(@RequestBody LoginDto loginDto){
        String response = authService.login(loginDto, RoleType.ROLE_RESTAURANT);
        System.out.println("Token: " + response);
        return ResponseEntity.ok(response);
    }
}
