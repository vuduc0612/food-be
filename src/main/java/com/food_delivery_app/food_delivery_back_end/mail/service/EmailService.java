package com.food_delivery_app.food_delivery_back_end.mail.service;

import com.food_delivery_app.food_delivery_back_end.config.RabbitMQConfig;
import com.food_delivery_app.food_delivery_back_end.mail.entity.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                emailMessage
        );
        System.out.println("Email đã được đưa vào queue: " + emailMessage.getSubject());
    }
}