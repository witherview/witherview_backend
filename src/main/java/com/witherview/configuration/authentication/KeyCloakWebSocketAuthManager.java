package com.witherview.configuration.authentication;

import lombok.AllArgsConstructor;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.keycloak.TokenVerifier;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.adapters.springsecurity.config.KeycloakSpringConfigResolverWrapper;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("websocket")
@AllArgsConstructor
public class KeyCloakWebSocketAuthManager implements AuthenticationManager {

    private final KeycloakSpringConfigResolverWrapper keycloakSpringConfigResolverWrapper;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JWSAuthenticationToken auth = (JWSAuthenticationToken) authentication;
        String tokenString = (String) auth.getCredentials();
        try {
            // 들어온 jwt 토큰값을 keycloak 설정파일을 토대로 resolve
            // 풀어낸 토큰값을 토대로 AccessToken이라는 객체를 만들어낸다.
            // 이 객체가 keycloak에서 custom 필드 (userId, email)를 추가할 때 사용한 영역임.
            // 따라서 제대로 파싱이 되었다면, 아래 커스텀 필드를 인식해야 한다.
            final KeycloakDeployment resolve = keycloakSpringConfigResolverWrapper.resolve(null);
            final AccessToken accessToken = AdapterTokenVerifier.verifyToken(tokenString, resolve);
            System.out.println("AdapterTokenVerifier 사용해서 accessToken 생성" + accessToken);
            if (accessToken.getOtherClaims().get("userId") == null || accessToken.getOtherClaims().get("email") == null) {
                // 이게 없으면, 인증할 수 없는 토큰
                auth.setAuthenticated(false);
            }
            else {
                // 이게 있으면, 인증된 토큰.
                auth.setAuthenticated(true);
            }
        } catch (VerificationException e) {
            e.printStackTrace();
            auth.setAuthenticated(false);
        }
        return auth;
    }
}
