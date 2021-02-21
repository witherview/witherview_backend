package com.witherview.account.filter;

import com.witherview.constant.SecurityConstant;
import com.witherview.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

    public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        if (authenticationToken != null) {
            // SecurityContext에 authentication값 적용.
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);
        }
        // request -> response 연결.
        chain.doFilter(request, response);
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);

        if (token == null) return null;

        token = token.substring(SecurityConstant.TOKEN_PREFIX.length());
        // 토큰에 저장된 값들 추출하기.

        Claims claims = new JwtUtils().getClaims(token);

        // 기본 형태의 토큰 - UsernamePassword는 기본 형태 토큰.
        // abstractAuthenticationToken 클래스 상속받아서 직접 구현 가능.
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims, null);
        return authentication;
    }

}
