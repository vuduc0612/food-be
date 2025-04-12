package com.food_delivery_app.food_delivery_back_end.modules.auth.controller;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.*;
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
@Tag(name = "Auth API", description = "Provides endpoints for authentication")
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
    public ResponseEntity<ResponseObject> registerUserCustomer(@RequestBody @Valid RegisterDto registerUserDto){
        RegisterResponse response = authService.register(registerUserDto, RoleType.ROLE_USER);
        return  ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Please check your email to verify your account")
                        .data(response)
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    @PostMapping("/customer/verify-otp")
    @Operation(summary = "Verify OTP", description = "Returns the status of OTP verification")
    public ResponseEntity<ResponseObject> verifyEmailCustomer(@RequestBody @Valid RegisterDto registerUserDto, @RequestParam String otp){
        boolean response = authService.verifyEmail(registerUserDto, otp, RoleType.ROLE_USER);
        if (!response) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .message("OTP verification failed")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("OTP verified successfully")
                        .data(response)
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    @PostMapping("/customer/resend-otp")
    @Operation(summary = "Resend OTP", description = "Returns the status of OTP resend")
    public ResponseEntity<ResponseObject> resendOtpCustomer(@RequestBody @Valid RegisterDto registerUserDto){
        boolean response = authService.resendOtp(registerUserDto, RoleType.ROLE_USER);
        if (!response) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .message("Account does not exist or already verified")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("OTP has been resent. Please check your email.")
                        .data(response)
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    @PostMapping("/customer/login")
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
    @PostMapping("/restaurant/verify-otp")
    @Operation(summary = "Verify OTP", description = "Returns the status of OTP verification")
    public ResponseEntity<ResponseObject> verifyEmailRestaurant(@RequestBody @Valid RegisterDto registerUserDto, @RequestParam String otp){
        boolean response = authService.verifyEmail(registerUserDto, otp, RoleType.ROLE_RESTAURANT);
        if (!response) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .message("OTP verification failed")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("OTP verified successfully")
                        .data(response)
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    @PostMapping("/restaurant/resend-otp")
    @Operation(summary = "Resend OTP", description = "Returns the status of OTP resend")
    public ResponseEntity<ResponseObject> resendOtpRestaurant(@RequestBody @Valid RegisterDto registerUserDto){
        boolean response = authService.resendOtp(registerUserDto, RoleType.ROLE_RESTAURANT);
        if (!response) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .message("Account does not exist or already verified")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("OTP has been resent. Please check your email.")
                        .data(response)
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }
    @PostMapping("/restaurant/login")
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
    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset", description = "Sends OTP to user email for password reset")
    public ResponseEntity<ResponseObject> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        boolean result = authService.forgotPassword(request);
        if (!result) {
            // Trả về thông báo không cụ thể để đảm bảo bảo mật
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("If your email exists in our system, an OTP has been sent.")
                            .status(HttpStatus.OK)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("OTP has been sent to your email. Please check your inbox.")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping("/verify-reset-password-otp")
    @Operation(summary = "Verify reset password OTP", description = "Verifies OTP for password reset")
    public ResponseEntity<ResponseObject> verifyResetPasswordOtp(@RequestBody @Valid VerifyResetOtpRequest request) {
        boolean verified = authService.verifyResetPasswordOtp(request);
        if (!verified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .message("Invalid OTP or OTP expired")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("OTP verified successfully. You can now reset your password.")
                        .data(verified)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Resets user password after OTP verification")
    public ResponseEntity<ResponseObject> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        boolean result = authService.resetPassword(request);
        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .message("Password reset failed. Please verify your OTP and try again.")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Password has been reset successfully")
                        .data(result)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping("/resend-reset-password-otp")
    @Operation(summary = "Resend reset password OTP", description = "Resends OTP for password reset")
    public ResponseEntity<ResponseObject> resendResetPasswordOtp(@RequestBody @Valid ForgotPasswordRequest request) {
        boolean result = authService.resendResetPasswordOtp(request);
        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .message("Failed to resend OTP. Please check your email address.")
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("OTP has been resent. Please check your email.")
                        .status(HttpStatus.OK)
                        .build()
        );
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Returns the status of user deletion")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id){
        authService.deleteAccount(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("User deleted successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
