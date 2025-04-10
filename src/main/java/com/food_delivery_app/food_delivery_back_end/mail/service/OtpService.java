package com.food_delivery_app.food_delivery_back_end.mail.service;

import com.food_delivery_app.food_delivery_back_end.mail.dto.OtpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    @Value("${stringee.api.key-sid}")
    private String apiKeySid;

    @Value("${stringee.api.key-secret}")
    private String apiKeySecret;

    @Value("${stringee.sender-id}")
    private String senderId;

    @Value("${stringee.sms-url}")
    private String smsUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OtpService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    // Tạo mã OTP ngẫu nhiên (6 chữ số)
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo số từ 100000 đến 999999
        return String.valueOf(otp);
    }

    // Gửi SMS OTP qua Stringee
    public OtpResponse sendOtpSms(String phoneNumber, String otp) {
        // Chuẩn hóa số điện thoại
        if (!phoneNumber.startsWith("+")) {
            if (phoneNumber.startsWith("0")) {
                phoneNumber = "+84" + phoneNumber.substring(1); // Bỏ số 0, thêm +84
            } else {
                phoneNumber = "+84" + phoneNumber;
            }
        } else if (phoneNumber.startsWith("+840")) {
            phoneNumber = "+84" + phoneNumber.substring(4); // Bỏ +840, giữ +84
        }

        // Tạo nội dung tin nhắn
        String messageBody = "Mã OTP của bạn là: " + otp + ". Hiệu lực trong 5 phút.";

        // Tạo header với JWT token đã có
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-STRINGEE-AUTH", "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLkhmMzBkV0w1ZndjNmc0SUFJdVFZUUlJVm5rVG9jQ3ktMTc0NDI2ODgyNSIsImlzcyI6IlNLLjAuSGYzMGRXTDVmd2M2ZzRJQUl1UVlRSUlWbmtUb2NDeSIsImV4cCI6MTc0Njg2MDgyNSwicmVzdF9hcGkiOnRydWV9.8gq6Y8vt28bJRbQM_bCUs0Ghrm8Tj_BJAdJh9OWO4tw");

        // Tạo body request
        Map<String, Object> body = new HashMap<>();
        body.put("from", senderId);
        body.put("to", phoneNumber);
        body.put("text", messageBody);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // Gọi API Stringee để gửi SMS
            ResponseEntity<String> response = restTemplate.exchange(
                    smsUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Parse response từ Stringee
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            int smsSent = responseJson.get("smsSent").asInt();

            if (smsSent > 0) {
                return new OtpResponse("OTP đã được gửi đến " + phoneNumber, "success");
            } else {
                return new OtpResponse("Không thể gửi SMS: Không có tin nhắn nào được gửi", "error");
            }
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            return new OtpResponse("Không thể gửi SMS: " + e.getMessage(), "error");
        }
    }
}