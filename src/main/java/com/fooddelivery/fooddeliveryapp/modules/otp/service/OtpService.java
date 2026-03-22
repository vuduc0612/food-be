package com.fooddelivery.fooddeliveryapp.modules.otp.service;

import com.fooddelivery.fooddeliveryapp.mail.entity.EmailMessage;
import com.fooddelivery.fooddeliveryapp.mail.service.EmailService;
import com.fooddelivery.fooddeliveryapp.modules.auth.entity.Account;
import com.fooddelivery.fooddeliveryapp.modules.otp.entity.OtpToken;
import com.fooddelivery.fooddeliveryapp.modules.otp.repository.OtpRepository;
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
