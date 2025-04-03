package com.food_delivery_app.food_delivery_back_end.modules.auth.controller;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterResponse;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/auth")
@Tag(name = "Users API", description = "Provides endpoints for customer")
@Validated
public class AuthController {
    @Autowired
    private AuthService authService;
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!!";
    }

    @PostMapping("/customer/register")
    @Operation(summary = "Register a new customer", description = "Returns the status registered customer")
    public ResponseEntity<ResponseObject> registerUser(@RequestBody @Valid RegisterDto registerUserDto){
        RegisterResponse response = authService.register(registerUserDto, RoleType.ROLE_USER);
        return  ResponseEntity.ok(
                ResponseObject.builder()
                        .message("User registered successfully")
                        .data(response)
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    @PostMapping("customer/login")
    @Operation(summary = "Login a user", description = "Return token to authenticate user")
    public ResponseEntity<ResponseObject> loginUser(@RequestBody @Valid LoginDto loginDto){
        String response = authService.login(loginDto, RoleType.ROLE_USER);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Login successfully")
                        .data(response)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping("/restaurant/register")
    @Operation(summary = "Register a new restaurant", description = "Returns the status registered restaurant")
    public ResponseEntity<ResponseObject> registerRestaurant(@RequestBody @Valid RegisterDto registerUserDto){
        RegisterResponse response = authService.register(registerUserDto, RoleType.ROLE_RESTAURANT);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Restaurant registered successfully")
                        .data(response)
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    @PostMapping("restaurant/login")
    @Operation(summary = "Login a user", description = "Return token to authenticate user")
    public ResponseEntity<ResponseObject> loginRestaurant(@RequestBody @Valid LoginDto loginDto){
        String response = authService.login(loginDto, RoleType.ROLE_RESTAURANT);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Login successfully")
                        .data(response)
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
