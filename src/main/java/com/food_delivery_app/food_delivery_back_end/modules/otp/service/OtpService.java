package com.food_delivery_app.food_delivery_back_end.modules.otp.service;

import com.food_delivery_app.food_delivery_back_end.mail.entity.EmailMessage;
import com.food_delivery_app.food_delivery_back_end.mail.service.EmailConsumer;
import com.food_delivery_app.food_delivery_back_end.mail.service.EmailService;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.otp.entity.OtpToken;
import com.food_delivery_app.food_delivery_back_end.modules.otp.repository.OtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    @Transactional
    public void generateAndSendOtp(Account account){
        otpRepository.deleteByAccountAndVerifiedFalse(account);
        String otpCode = generateOtpCode();
        OtpToken otpToken = OtpToken.builder()
                .code(otpCode)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .account(account)
                .build();
        otpRepository.save(otpToken);

        //Send OTP to email
        String htmlBody = emailService.buildOtpEmailBody(otpToken.getCode());
        EmailMessage message = new EmailMessage();
        message.setTo(account.getEmail());
        message.setSubject("Xác thực đăng ký tài khoản");
        message.setBody(htmlBody);

        emailService.sendEmail(message);

    }
    public boolean verifyOtp(Account account, String otpCode) {
        Optional<OtpToken> otpOptional = otpRepository.findByAccountAndVerifiedFalseAndExpiryTimeAfter(
                account, LocalDateTime.now());

        if (otpOptional.isPresent()) {
            OtpToken otp = otpOptional.get();
            if (otp.getCode().equals(otpCode)) {
                otp.setVerified(true);
                otpRepository.save(otp);
                return true;
            }
        }
        return false;
    }

    private String generateOtpCode() {
        // Tạo mã OTP ngẫu nhiên 6 số
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
