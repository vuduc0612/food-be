package com.food_delivery_app.food_delivery_back_end.security;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    public String generateToken(String email){
        return "token";
    }
}
