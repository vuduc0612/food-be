package com.food_delivery_app.food_delivery_back_end.service.Impl;

import com.food_delivery_app.food_delivery_back_end.dto.RegisterUserDto;
import com.food_delivery_app.food_delivery_back_end.entity.User;
import com.food_delivery_app.food_delivery_back_end.repostitory.UserRepository;
import com.food_delivery_app.food_delivery_back_end.service.AuthService;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    @Override
    public String register(RegisterUserDto userAuthDto) {
        String email = userAuthDto.getEmail();
        if(userRepository.existsByEmail(email)){
            return "Email already exists";
        }
        String phoneNumber =  userAuthDto.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            return "Phone number already exists";
        }

        String password = userAuthDto.getPassword();
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        return "User registered successfully";
    }
}
