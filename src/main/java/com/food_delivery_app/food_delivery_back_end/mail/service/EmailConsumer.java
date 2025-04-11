package com.food_delivery_app.food_delivery_back_end.mail.service;

import com.food_delivery_app.food_delivery_back_end.config.RabbitMQConfig;
import com.food_delivery_app.food_delivery_back_end.mail.entity.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final JavaMailSender mailSender;


    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeEmailFromQueue(EmailMessage emailMessage) {
        try {
            sendEmail(emailMessage);
            System.out.println("Email đã được gửi thành công: " + emailMessage.getSubject());
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email: " + e);
            // Ở đây bạn có thể thêm logic để xử lý email lỗi, ví dụ: lưu vào DB để thử lại sau
        }
    }

    private void sendEmail(EmailMessage emailMessage) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(emailMessage.getTo());
//        message.setSubject(emailMessage.getSubject());
//        message.setText(emailMessage.getBody());
//
//        mailSender.send(message);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getBody(), true); // gửi dạng HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


}