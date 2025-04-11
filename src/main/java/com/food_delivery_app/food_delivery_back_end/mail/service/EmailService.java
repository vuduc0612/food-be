package com.food_delivery_app.food_delivery_back_end.mail.service;

import com.food_delivery_app.food_delivery_back_end.config.RabbitMQConfig;
import com.food_delivery_app.food_delivery_back_end.mail.entity.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RabbitTemplate rabbitTemplate;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                emailMessage
        );
        System.out.println("Email đã được đưa vào queue: " + emailMessage.getSubject());
    }
    public String buildOtpEmailBody(String otp) {
        Context context = new Context();
        context.setVariable("otp", otp);
        return templateEngine.process("mail/otp_template", context); // tên file HTML không cần .html
    }
}