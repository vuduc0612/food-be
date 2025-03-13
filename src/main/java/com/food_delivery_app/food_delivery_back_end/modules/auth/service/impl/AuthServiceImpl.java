package com.food_delivery_app.food_delivery_back_end.modules.auth.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.exception.AccountNotFoundException;
import com.food_delivery_app.food_delivery_back_end.exception.ForbiddenException;
import com.food_delivery_app.food_delivery_back_end.exception.InvalidCredentialsException;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.AccountRole;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRepository;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRoleRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import com.food_delivery_app.food_delivery_back_end.security.UserPrincipal;
import com.food_delivery_app.food_delivery_back_end.utils.JwtTokenProvider;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private PasswordEncoder passwordEncoder;
    private AccountRepository accountRepository;
    private AccountRoleRepository accountRoleRepository;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    @Override
    public String register(RegisterDto registerDto, RoleType roleType) {
        String email = registerDto.getEmail();
        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        Account account;
        if (existingAccount.isPresent()) {
            account = existingAccount.get();

            // check existing role
            boolean hasRole = accountRoleRepository.existsByAccountAndRoleType(account, roleType);
            if (hasRole) {
                return "Account already has this role";
            }
        }
        else {
            // create account
            account = new Account();
            account.setEmail(registerDto.getEmail());
            account.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            account.setPhoneNumber(registerDto.getPhoneNumber());
            account.setStatus("ACTIVE");
            account.setCreatedAt(LocalDateTime.now());
            accountRepository.save(account);
        }
        // add role to account
        AccountRole accountRole = new AccountRole();
        accountRole.setAccount(account);
        accountRole.setRoleType(roleType);
        accountRole.setActive(true);
        accountRoleRepository.save(accountRole);

        // create user or restaurant
        System.out.println(roleType);
        switch (roleType) {
            case ROLE_USER:
                createUser(account, registerDto);
                break;
            case ROLE_RESTAURANT:
                createRestaurant(account, registerDto);
                break;
        }
        return "User registered successfully";
    }

    @Override
    public String login(LoginDto loginDto, RoleType roleType) {
        String email = loginDto.getEmail();

        if(!accountRepository.existsByEmail(email)){
            throw new AccountNotFoundException("Account not found with email: " + email);
        }

        Account account = accountRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with email: " + email));

        // check password
        if (!passwordEncoder.matches(loginDto.getPassword(), account.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }

        boolean hasRole = account.getAccountRoles().stream()
                .anyMatch(r -> r.getRoleType() == roleType && r.isActive());
        if (!hasRole) {
            throw new ForbiddenException("You don't have access to this role");
        }

        account.setLastLogin(LocalDateTime.now());
        accountRepository.save(account);
        return jwtTokenProvider.generateToken(email, roleType);
    }

    private void createUser(Account account, RegisterDto dto) {
        if (account.getUser() == null) {
            User user = new User();
            user.setAccount(account);
            userRepository.save(user);
        }
    }



    private void createRestaurant(Account account, RegisterDto dto) {
        if (account.getRestaurant() == null) {
            Restaurant restaurant = new Restaurant();
            restaurant.setAccount(account);
            restaurantRepository.save(restaurant);
        }
    }
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        if(authentication != null && authentication.getPrincipal() instanceof UserPrincipal){
            UserPrincipal userPrincipal =  (UserPrincipal) authentication.getPrincipal();
            System.out.println("UserPrincipal: " + userPrincipal);
            Long accountId = userPrincipal.getId();
            return userRepository.findByAccountId(accountId)
                    .orElseThrow(() -> new EntityExistsException("User not found"));
        }
        return null;
    }

    @Override
    public Restaurant getCurrentRestaurant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserPrincipal){
            UserPrincipal userPrincipal =  (UserPrincipal) authentication.getPrincipal();
            Long accountId = userPrincipal.getId();
            return restaurantRepository.findByAccountId(accountId)
                    .orElseThrow(() -> new EntityExistsException("Restaurant not found"));
        }
        return null;
    }
}
