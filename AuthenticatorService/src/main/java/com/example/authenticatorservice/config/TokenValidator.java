package com.example.authenticatorservice.config;

import com.example.authenticatorservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    @Autowired
    private JwtService jwtService;

    public boolean isTokenValid(String token) {
        try {
            Claims claims = jwtService.extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
