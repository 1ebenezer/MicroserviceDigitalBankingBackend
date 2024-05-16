package com.example.accountservice.DTOs;

import com.example.accountservice.entity.Enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    //       private Integer accNumber;
//    private String authorizationHeader;
    private AccountType accType;
//    private Integer userId;
//    //    private Double balance;
//    private String token; // Extracted token from the authorization header
//
//    public void extractToken(HttpServletRequest request, JwtValidator jwtValidator) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Missing or invalid authorization header");
//        }
//        String token = authHeader.substring(7);
//        if (!jwtValidator.isTokenValid(token)) {
//            throw new RuntimeException("Invalid authorization token");
//        }
//        this.token = token;
//        this.userId = Integer.parseInt(jwtValidator.extractUserId(token));
//    }
}


