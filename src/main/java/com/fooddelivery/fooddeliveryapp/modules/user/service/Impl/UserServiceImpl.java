package com.fooddelivery.fooddeliveryapp.modules.user.service.Impl;

import com.fooddelivery.fooddeliveryapp.constant.RoleType;
import com.fooddelivery.fooddeliveryapp.exception.DataNotFoundException;
import com.fooddelivery.fooddeliveryapp.modules.auth.entity.Account;
import com.fooddelivery.fooddeliveryapp.modules.auth.entity.AccountRole;
import com.fooddelivery.fooddeliveryapp.modules.auth.repository.AccountRepository;
import com.fooddelivery.fooddeliveryapp.modules.auth.repository.AccountRoleRepository;
import com.fooddelivery.fooddeliveryapp.modules.user.dto.UserResponseDto;
import com.fooddelivery.fooddeliveryapp.modules.user.entity.User;
import com.fooddelivery.fooddeliveryapp.modules.user.repository.UserRepository;
import com.fooddelivery.fooddeliveryapp.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findUsersByActiveRole(RoleType.ROLE_USER, pageable);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    @Override
    public UserResponseDto updateUser(Long id, UserResponseDto userResponseDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userResponseDto.getName());
        user.setAddress(userResponseDto.getAddress());
        Account account = accountRepository.findAccountByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setPhoneNumber(userResponseDto.getPhoneNumber());
        accountRepository.save(account);
        userRepository.save(user);

        return UserResponseDto.builder()
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
