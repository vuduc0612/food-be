package com.food_delivery_app.food_delivery_back_end.service.Impl;

import com.food_delivery_app.food_delivery_back_end.dto.UserDto;
import com.food_delivery_app.food_delivery_back_end.entity.User;
import com.food_delivery_app.food_delivery_back_end.repostitory.UserRepository;
import com.food_delivery_app.food_delivery_back_end.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
