package com.witherview.account.configuration;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class JWSAuthenticationToken extends AbstractAuthenticationToken implements Authentication {

    private String token;
    private User principal;

    public JWSAuthenticationToken(String token) {
        this(null, token, null);
    }

    public JWSAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String token, User principal) {
        super(authorities);
        this.token = token;
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
