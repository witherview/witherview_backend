package com.witherview.utils;

import com.witherview.constant.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private final Key key;
    public JwtUtils() {
        this.key = Keys.hmacShaKeyFor(SecurityConstant.SECRET.getBytes());
    }
    // input 값으로 jwt 토큰 생성하기.
    public String createToken(String email) {
        var token = Jwts.builder()
//                .setSubject("user", email)
                .claim("email", email)
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstant.EXPIRATION_TIME)) // 1시간)
                .signWith(this.key, SignatureAlgorithm.HS256)
                .compact()
                ;

        return token;
    }

    public Claims getClaims(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                ;
        return claims;
    }

}
