package com.fooddelivery.fooddeliveryapp.mail.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;


public interface MailService {
    void sendOrderSuccessEmail(String to, String customerName) throws MessagingException, IOException;
}
