package com.food_delivery_app.food_delivery_back_end.utils;

import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${jwtSecret}")
    private String secret;
    @Value("${jwtExpirationMs}")
    private Long jwtExpirationInMs;
    private Key key;
    public String generateToken(String email, RoleType roleType){
        Date curentDate = new Date();
        Date expiredate = new Date(curentDate.getTime() + jwtExpirationInMs);
        this.key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(curentDate)
                .claim("role", roleType.name())
                .setExpiration(expiredate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public String getEmailFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validationToken(String token){
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
