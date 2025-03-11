package com.food_delivery_app.food_delivery_back_end.controller;

import com.food_delivery_app.food_delivery_back_end.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.dto.RegisterUserDto;
import com.food_delivery_app.food_delivery_back_end.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/auth")
@Tag(name = "Users API", description = "Provides endpoints for customer")
public class AuthController {
    @Autowired
    private AuthService userService;
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!!";
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Returns the status registered user")
    public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto){
        String response = userService.register(registerUserDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Return token to authenticate user")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        String response = userService.login(loginDto);
        System.out.println("Token: " + response);
        return  ResponseEntity.ok(response);
    }
}
