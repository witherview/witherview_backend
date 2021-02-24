package com.witherview.utils;

import com.witherview.account.exception.CustomJwtRuntimeException;
import com.witherview.constant.SecurityConstant;
import com.witherview.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
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
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (io.jsonwebtoken.security.SignatureException e ){
            log.error("Token :" + token +" - Invalid Jwt Signiture.");
            throw new CustomJwtRuntimeException(ErrorCode.FORBIDDEN);
        } catch (SecurityException e) {
            log.error("Token :" + token +" - Invalid Jwt Signiture.");
            throw new CustomJwtRuntimeException(ErrorCode.FORBIDDEN);
        } catch (MalformedJwtException e) {
            log.error("Token :" + token + " - Invalid Jwt token value.");
            throw new CustomJwtRuntimeException(ErrorCode.FORBIDDEN);
        } catch (ExpiredJwtException e) {
            log.error("Token :" + token + " - Expired Jwt Token.");
            throw new CustomJwtRuntimeException(ErrorCode.FORBIDDEN);
        } catch (UnsupportedJwtException e) {
            log.error("Token :" + token +" - Unsupported Jwt Token.");
            throw new CustomJwtRuntimeException(ErrorCode.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            log.error("Token :" + token +" - Jwt token compact of handler are invalid.");
            throw new CustomJwtRuntimeException(ErrorCode.FORBIDDEN);
        }

    }

}
