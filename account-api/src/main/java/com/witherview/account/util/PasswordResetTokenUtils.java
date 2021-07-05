package com.witherview.account.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import util.SecurityConstant;

import java.security.Key;
import java.util.Date;

@Component
public class PasswordResetTokenUtils {
    private static Key key = Keys.hmacShaKeyFor(SecurityConstant.SECRET.getBytes());

    public static String generatePasswordResetToken(String email) {
        var token = Jwts.builder()
                .setSubject(email)
                // Expire Time : 현재 시간으로부터 1시간.
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public static boolean isTokenExpired(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
        var expireDate = claims.getExpiration();
        var today = new Date();
        return expireDate.before(today);
    }

    public static String getUserEmail(String token) {
        var email = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
        return email;
    }
}
