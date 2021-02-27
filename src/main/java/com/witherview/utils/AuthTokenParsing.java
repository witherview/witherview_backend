package com.witherview.utils;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public class AuthTokenParsing {
    public static String getAuthClaimValue(Authentication auth, String key) {
        Claims claims = (Claims) auth.getPrincipal();
        String value = claims.get(key, String.class);
        return value;
    }
}
