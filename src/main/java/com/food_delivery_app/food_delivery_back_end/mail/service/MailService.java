package com.food_delivery_app.food_delivery_back_end.mail.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;


public interface MailService {
    void sendOrderSuccessEmail(String to, String customerName) throws MessagingException, IOException;
}
