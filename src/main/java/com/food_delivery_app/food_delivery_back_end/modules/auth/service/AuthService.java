package com.food_delivery_app.food_delivery_back_end.modules.auth.service;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.*;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;


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
