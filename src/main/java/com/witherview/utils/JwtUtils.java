package com.witherview.utils;

import com.witherview.account.exception.InvalidJwtTokenException;
import com.witherview.constant.SecurityConstant;
import com.witherview.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {
    private final Key key;
    public JwtUtils() {
        this.key = Keys.hmacShaKeyFor(SecurityConstant.SECRET.getBytes());
    }

    // input 값으로 jwt 토큰 생성하기.
    public String createToken(String email, String userId) {
        var token = Jwts.builder()
//                .setSubject("user", email)
                .claim("email", email)
                .claim("userId", userId)
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
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (SecurityException e) {
            log.error("Token :" + token +" - Invalid Jwt Signiture.");
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (MalformedJwtException e) {
            log.error("Token :" + token + " - Invalid Jwt token value.");
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Token :" + token + " - Expired Jwt Token.");
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Token :" + token +" - Unsupported Jwt Token.");
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("Token :" + token + " - Jwt token compact of handler are invalid.");
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public Boolean verifyToken(String header) {
        System.out.println("들어온 header " + header);
        if (header == null) {
            throw new InvalidJwtTokenException(ErrorCode.INVALID_TOKEN);
        }
        if (!header.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        }
        var token = header.substring(SecurityConstant.TOKEN_PREFIX.length());
        return getClaims(token) != null;
    }

    public Boolean isTokenExpired(String token) {
        var claims = getClaims(token);
        var expiredDate = claims.getExpiration();
        var expiredLocalDateTime = expiredDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        var now = LocalDateTime.now();
        return now.isAfter(expiredLocalDateTime);
    }
}
