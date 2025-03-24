package com.food_delivery_app.food_delivery_back_end.modules.user.service.Impl;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.AccountRole;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRepository;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRoleRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.dto.UserDto;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    @Override
    public Page<UserDto> getAllUsers(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findAllUsersWithEmailAndUsername(pageable);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDto.getName());
        user.setAddress(userDto.getAddress());
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setPhoneNumber(userDto.getPhoneNumber());
        accountRepository.save(account);
        userRepository.save(user);

        return UserDto.builder()
                .id(user.getId())
                .email(account.getEmail())
                .name(user.getUsername())
                .address(user.getAddress())
                .phoneNumber(account.getPhoneNumber())
                .build();
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return UserDto.builder()
                .id(user.getId())
                .email(account.getEmail())
                .name(user.getUsername())
                .address(user.getAddress())
                .phoneNumber(account.getPhoneNumber())
                .build();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Set<AccountRole> accountRole = account.getAccountRoles();
        for(AccountRole role : accountRole){
            if(role.getRoleType().name().equals("ROLE_USER")){
                role.setActive(false);
                break;
            }
        }
        account.setAccountRoles(accountRole);
        accountRepository.save(account);
    }
}
