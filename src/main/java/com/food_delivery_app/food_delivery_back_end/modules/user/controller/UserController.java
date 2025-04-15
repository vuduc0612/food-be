package com.food_delivery_app.food_delivery_back_end.modules.user.controller;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.service.UserService;
import com.food_delivery_app.food_delivery_back_end.response.CustomPageResponse;
import com.food_delivery_app.food_delivery_back_end.response.ResponseObject;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users")
@AllArgsConstructor
public class UserController {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private UserService userService;
    private AuthService authService;

    @GetMapping("/fake-data")
    public ResponseEntity<ResponseObject> fakeData(){
        Faker faker = new Faker(new Locale("vi"));

        // Tạo 10 Users
        for (int i = 0; i < 1000; i++) {
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().cellPhone();
            String fullName = faker.name().fullName();

            RegisterDto registerDto = new RegisterDto(email, phone, fullName, "password123");
            authService.register(registerDto, RoleType.ROLE_USER);

        }
        List<User> users = userRepository.findAll();
        for(User user : users){
            user.setUsername(faker.name().fullName());
            user.setAddress(faker.address().fullAddress());
            userRepository.save(user);
        }

        System.out.println("Generated 100 fake users successfully!");
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Generated 100 fake users successfully!")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<CustomPageResponse<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page
    ){
        Page<UserResponseDto> userDtoPage =  userService.getAllUsers(page, limit);
        return ResponseEntity.ok(
                CustomPageResponse.<UserResponseDto>builder()
                        .message("Get all users successfully")
                        .status(HttpStatus.OK)
                        .data(userDtoPage.getContent())
                        .currentPage(userDtoPage.getNumber())
                        .totalItems(userDtoPage.getTotalElements())
                        .totalPages(userDtoPage.getTotalPages())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUser(@PathVariable Long id){
        UserResponseDto userResponseDto = userService.getUser(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get user successfully")
                        .data(userResponseDto)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getCurrentUser(){
        User user = authService.getCurrentUser();
//        System.out.println(user);
        UserResponseDto userResponseDto = userService.getUser(user.getId());
//        System.out.println(userResponseDto);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get current user successfully")
                        .data(userResponseDto)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> updateUser(
            @PathVariable Long id,
            @RequestBody UserResponseDto userResponseDto
    ){
        UserResponseDto updatedUser = userService.updateUser(id, userResponseDto);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update user successfully")
                        .data(updatedUser)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete user successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }


}
