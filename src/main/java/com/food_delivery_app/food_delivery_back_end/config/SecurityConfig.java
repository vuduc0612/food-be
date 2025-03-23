package com.food_delivery_app.food_delivery_back_end.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.food_delivery_app.food_delivery_back_end.utils.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;


@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                String.format("%s/auth/**", apiPrefix),
                                "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/users/**", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/restaurants/**", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/dishes/**", apiPrefix)).permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new JavaTimeModule());
                            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                                    "Unauthorized");
                            response.getWriter().write(mapper.writeValueAsString(errorResponse));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new JavaTimeModule());
                            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                                    "Forbidden");
                            response.getWriter().write(mapper.writeValueAsString(errorResponse));
                        })
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean()
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private int status;
        private String message;
        private LocalDateTime timestamp = LocalDateTime.now();

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }


}
