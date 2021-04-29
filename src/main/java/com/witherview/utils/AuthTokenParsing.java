package com.witherview.utils;

import com.witherview.account.exception.InvalidJwtTokenException;
import com.witherview.configuration.authentication.JWSAuthenticationToken;
import com.witherview.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthTokenParsing {
    public static String getAuthClaimValue(Authentication auth, String key) {
        if (auth instanceof JWSAuthenticationToken) {
            if (auth.isAuthenticated()) {
                return (String) auth.getCredentials();
            }
            else {
                throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
            }
        }
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
//        System.out.println(token.getTokenAttributes());
        var value = (String) token.getTokenAttributes().get(key);
        return value;
    }
}
