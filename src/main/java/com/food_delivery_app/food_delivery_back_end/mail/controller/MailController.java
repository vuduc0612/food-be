package com.food_delivery_app.food_delivery_back_end.mail.controller;

import com.food_delivery_app.food_delivery_back_end.mail.entity.EmailMessage;
import com.food_delivery_app.food_delivery_back_end.mail.service.EmailService;
import com.food_delivery_app.food_delivery_back_end.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/mail")
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
