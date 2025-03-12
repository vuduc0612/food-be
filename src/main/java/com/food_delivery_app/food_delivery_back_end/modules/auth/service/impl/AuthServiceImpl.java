package com.food_delivery_app.food_delivery_back_end.modules.auth.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
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
import com.food_delivery_app.food_delivery_back_end.utils.JwtTokenProvider;
import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
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

            // Kiểm tra xem account đã có role này chưa
            boolean hasRole = accountRoleRepository.existsByAccountAndRoleType(account, roleType);
            if (hasRole) {
                return "This account already has " + roleType + " role";
            }
        }
        else {
            // Tạo account mới nếu email chưa tồn tại
            account = new Account();
            account.setEmail(registerDto.getEmail());
            account.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            account.setPhoneNumber(registerDto.getPhoneNumber());
            account.setStatus("ACTIVE");
            account.setCreatedAt(LocalDateTime.now());
            accountRepository.save(account);
        }
        // Thêm role mới cho account
        AccountRole accountRole = new AccountRole();
        accountRole.setAccount(account);
        accountRole.setRoleType(roleType);
        accountRole.setActive(true);
        accountRoleRepository.save(accountRole);

        // Tạo hoặc cập nhật profile tương ứng với roleType
        System.out.println(roleType);
        switch (roleType) {
            case ROLE_USER:
                createUser(account, registerDto);
                break;
//            case ROLE_DRIVER:
////                createDriverProfile(account, registerDto);
////                break;
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
            return "User not found";
        }
        Account account = accountRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(loginDto.getPassword(), account.getPassword())) {
            return "Wrong password";
        }
        boolean hasRole = account.getAccountRoles().stream()
                .anyMatch(r -> r.getRoleType() == roleType && r.isActive());
        if (!hasRole) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(new ErrorResponse("You don't have access to this role"));
            return "You don't have access to this role";
        }
        account.setLastLogin(LocalDateTime.now());
        accountRepository.save(account);
        return jwtTokenProvider.generateToken(email, roleType);
    }

    private void createUser(Account account, RegisterDto dto) {
        // Kiểm tra xem account đã có user profile chưa
        if (account.getUser() == null) {
            User user = new User();
            user.setAccount(account);
            userRepository.save(user);
        }
    }



    private void createRestaurant(Account account, RegisterDto dto) {
        // Kiểm tra xem account đã có restaurant profile chưa
        if (account.getRestaurant() == null) {
            Restaurant restaurant = new Restaurant();
            restaurant.setAccount(account);
            restaurantRepository.save(restaurant);
        }
    }
}
