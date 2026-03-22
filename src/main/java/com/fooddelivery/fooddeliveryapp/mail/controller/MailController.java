package com.fooddelivery.fooddeliveryapp.mail.controller;

import com.fooddelivery.fooddeliveryapp.mail.entity.EmailMessage;
import com.fooddelivery.fooddeliveryapp.mail.service.EmailService;
import com.fooddelivery.fooddeliveryapp.mail.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/mail")
@Tag(name = "Mail API", description = "Provides endpoints for mail")
public class MailController {
    private final MailService mailService;
    private final EmailService emailService;

    @PostMapping("/test-mail")
    public ResponseEntity<String> testMail() throws Exception {
        mailService.sendOrderSuccessEmail("vuhuuduc1206@gmail.com", "Nguyễn Văn A");
        return ResponseEntity.ok("Mail sent!");
    }


    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailMessage emailMessage) {
        emailService.sendEmail(emailMessage);
        return ResponseEntity.ok("Email đã được đưa vào hàng đợi để xử lý");
    }

}
