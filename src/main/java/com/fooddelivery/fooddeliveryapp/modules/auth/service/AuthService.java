package com.fooddelivery.fooddeliveryapp.modules.auth.service;

import com.fooddelivery.fooddeliveryapp.constant.RoleType;
import com.fooddelivery.fooddeliveryapp.modules.auth.dto.*;
import com.fooddelivery.fooddeliveryapp.modules.restaurant.entity.Restaurant;
import com.fooddelivery.fooddeliveryapp.modules.user.entity.User;


public interface AuthService {
    RegisterResponse register(RegisterDto userAuthDto, RoleType roleType);
    String login(LoginDto loginDto, RoleType roleType);
    User getCurrentUser();
    Restaurant getCurrentRestaurant();
    void deleteAccount(Long accountId);

    boolean verifyEmail(RegisterDto userAuthDto, String otp, RoleType roleType);
    boolean resendOtp(RegisterDto userAuthDto, RoleType roleType);
    boolean forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    boolean verifyResetPasswordOtp(VerifyResetOtpRequest verifyResetOtpRequest);
    boolean resetPassword(ResetPasswordRequest resetPasswordRequest);
    boolean resendResetPasswordOtp(ForgotPasswordRequest forgotPasswordRequest);
}
