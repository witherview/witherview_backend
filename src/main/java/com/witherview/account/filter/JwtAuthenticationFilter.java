package com.witherview.account.filter;

import com.witherview.constant.SecurityConstant;
import com.witherview.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authenticationToken = getJwtTokenAuthentication(request, response);
        if (authenticationToken != null) {
            // SecurityContext에 authentication값 적용.
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getJwtTokenAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);
        if (token == null || !token.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            return null;
        }
//        // 토큰 분리.
        token = token.substring(SecurityConstant.TOKEN_PREFIX.length());
        Claims claims = null;
        try {
            claims = new JwtUtils().getClaims(token);
            if (claims != null) {
                // todo: 토큰 검증로직이 여기 들어가야 한다.
                //  redis에서 userId == email 인지만 검사하는 걸로 할까?
                //  이중암호를 걸었다면, 해제로직도 여기서 있어야 한다.
                //  매번 데이터베이스를 조회하는 것보다 빠를 만한 방식을 생각해보자.
                String email = claims.get("email", String.class);
                String userId = claims.get("userId", String.class);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(claims, null, new ArrayList<>());
                return authentication;
            }
        } catch (Exception e) {
            log.error("JwtToken Error in Authentication Filter. " + e.getMessage());
        }
        return null;
    }
}
