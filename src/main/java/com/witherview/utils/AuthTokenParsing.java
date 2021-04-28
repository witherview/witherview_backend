package com.witherview.utils;

import com.witherview.configuration.authentication.JWSAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthTokenParsing {
    public static String getAuthClaimValue(Authentication auth, String key) {
        if (auth instanceof JWSAuthenticationToken && auth.isAuthenticated()) {
            return (String) auth.getCredentials();
        }
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
//        System.out.println(token.getTokenAttributes());
        var value = (String) token.getTokenAttributes().get(key);
        return value;
    }
}
