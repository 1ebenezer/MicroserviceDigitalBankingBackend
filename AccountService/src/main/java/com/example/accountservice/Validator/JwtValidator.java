package com.example.accountservice.Validator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.List;

@Service
public class JwtValidator {

    //    private static final String SECRET_KEY = "FDA6A5B0436E06FB69F8A89FA6E2E89798E164935BFB4AD099748C2480E4AE2A";
    private SecretKey key;

    private static final String SECRET_KEY = "FDA6A5B0436E06FB69F8A89FA6E2E89798E164935BFB4AD099748C2480E4AE2A";

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


//    @PostConstruct
//    public void init() {
//        String secretKey = "FDA6A5B0436E06FB69F8A89FA6E2E89798E164935BFB4AD099748C2480E4AE2A";
//        key = Keys.hmacShaKeyFor(secretKey.getBytes());
//    }

    public String extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId").toString();
    }

    public List<String> extractUserRoleFromToken(String token) {

        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public boolean isTokenValid(String token) {
        System.out.println(key);
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
