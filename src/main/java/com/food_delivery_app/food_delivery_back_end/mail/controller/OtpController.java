package com.food_delivery_app.food_delivery_back_end.mail.controller;

import com.food_delivery_app.food_delivery_back_end.mail.dto.OtpResponse;
import com.food_delivery_app.food_delivery_back_end.mail.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/sms")
public class OtpController {

    private final OtpService otpService;

    @GetMapping("/test")
    public String test() {
        return "Test SMS service";
    }

    @PostMapping("/send")
    public OtpResponse sendOtp(@RequestParam String phoneNumber) {
        // Chuẩn hóa số điện thoại
        if (!phoneNumber.startsWith("+")) {
            if (phoneNumber.startsWith("0")) {
                phoneNumber = "+84" + phoneNumber.substring(1); // Thay số 0 bằng +84
            } else {
                phoneNumber = "+84" + phoneNumber; // Thêm +84 nếu không có
            }
        }
        String otp = otpService.generateOtp();
        return otpService.sendOtpSms(phoneNumber, otp);
    }
}