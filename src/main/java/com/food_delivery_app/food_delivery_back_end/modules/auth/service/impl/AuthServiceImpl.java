package com.food_delivery_app.food_delivery_back_end.modules.auth.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import com.food_delivery_app.food_delivery_back_end.exception.DataNotFoundException;
import com.food_delivery_app.food_delivery_back_end.exception.EntityExistsException;
import com.food_delivery_app.food_delivery_back_end.exception.ForbiddenException;
import com.food_delivery_app.food_delivery_back_end.exception.InvalidCredentialsException;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.LoginDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterResponse;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.AccountRole;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRepository;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRoleRepository;
import com.food_delivery_app.food_delivery_back_end.modules.otp.service.OtpService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
import com.food_delivery_app.food_delivery_back_end.security.UserPrincipal;
import com.food_delivery_app.food_delivery_back_end.utils.JwtTokenProvider;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import lombok.AllArgsConstructor;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OtpService otpService;

    @Override
    public RegisterResponse register(RegisterDto registerDto, RoleType roleType) {
        String email = registerDto.getEmail();
        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        Account account;
        if (existingAccount.isPresent()) {
            account = existingAccount.get();

            // check existing role
            boolean hasRole = accountRoleRepository.existsByAccountAndRoleType(account, roleType);
            if (hasRole && account.getStatus().equals("ACTIVE")) {
                throw new EntityExistsException("Account already has role: " + roleType);
            }
            System.out.println("Send OTP to existing account");
            otpService.generateAndSendOtp(account);

        }
        else {
            // create account
            account = new Account();
            account.setEmail(registerDto.getEmail());
            account.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            account.setPhoneNumber(registerDto.getPhoneNumber());
            account.setStatus("PENDING");
            account.setCreatedAt(LocalDateTime.now());
            accountRepository.save(account);
            System.out.println("Send OTP to new account");
            otpService.generateAndSendOtp(account);
        }
        return RegisterResponse.builder()
                .email(account.getEmail())
                .build();

    }
    @Override
    public boolean verifyOtp(RegisterDto registerDto, String otpCode, RoleType roleType) {
        Optional<Account> accountOptional = accountRepository.findByEmail(registerDto.getEmail());
        if (accountOptional.isEmpty()) return false;

        Account account = accountOptional.get();
        boolean verified = otpService.verifyOtp(account, otpCode);

        if (!verified) return false;

        // Cập nhật trạng thái đã xác thực
        account.setStatus("ACTIVE");
        accountRepository.save(account);

        // Gán role sau khi xác thực OTP
        AccountRole accountRole = new AccountRole();
        accountRole.setAccount(account);
        accountRole.setRoleType(roleType);
        accountRole.setActive(true);
        accountRoleRepository.save(accountRole);

        // Tạo đối tượng phụ (User hoặc Restaurant)
        switch (roleType) {
            case ROLE_USER:
                createUser(account, registerDto);
                break;
            case ROLE_RESTAURANT:
                createRestaurant(account, registerDto);
                break;
        }

        return true;
    }


    @Override
    public String login(LoginDto loginDto, RoleType roleType) {
        String email = loginDto.getEmail();

        if(!accountRepository.existsByEmail(email)){
            throw new DataNotFoundException(("Account not found with email: " + email));
        }

        Account account = accountRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Account not found with email: " + email));

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

    private void createUser(Account account, RegisterDto registerDto) {
        if (account.getUser() == null) {
            User user = new User();
            user.setAccount(account);
            user.setUsername(registerDto.getFullName());

            userRepository.save(user);
        }
    }



    private void createRestaurant(Account account, RegisterDto registerDto) {
        if (account.getRestaurant() == null) {
            Restaurant restaurant = new Restaurant();
            restaurant.setAccount(account);
            restaurant.setName(registerDto.getFullName());
            restaurantRepository.save(restaurant);
        }
    }
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //System.out.println("Authentication: " + authentication);
        if(authentication != null && authentication.getPrincipal() instanceof UserPrincipal){
            UserPrincipal userPrincipal =  (UserPrincipal) authentication.getPrincipal();
            //System.out.println("UserPrincipal: " + userPrincipal);
            Long accountId = userPrincipal.getId();
            return userRepository.findByAccountId(accountId)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));
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
                    .orElseThrow(() -> new DataNotFoundException("Restaurant not found"));
        }
        return null;
    }
}
