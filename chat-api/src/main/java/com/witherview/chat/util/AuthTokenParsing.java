package com.witherview.chat.util;

import exception.ErrorCode;
import exception.account.InvalidJwtTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthTokenParsing {
    public static String getAuthClaimValue(Authentication auth, String key) {
        if (!auth.isAuthenticated()) {
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        }
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
//        System.out.println(token.getTokenAttributes());
        var value = (String) token.getTokenAttributes().get(key);
        return value;
    }
}
