package com.witherview.account.filter;

import com.witherview.account.AccountService;
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
import java.util.ArrayList;
import java.util.Optional;


public class JwtAuthenticationFilter extends BasicAuthenticationFilter {


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authenticationToken = getJwtTokenAuthentication(request);
        if (authenticationToken != null) {
            // SecurityContext에 authentication값 적용.
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);
        }
        // request -> response 연결.
        chain.doFilter(request, response);
    }
    private UsernamePasswordAuthenticationToken getJwtTokenAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);
        System.out.println(token);
        if (token == null || !token.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            return null;
        }
        // 토큰 분리.
        token = token.substring(SecurityConstant.TOKEN_PREFIX.length());

        // todo: 토큰형식이 잘못되었을 때 에러나는 지점. 토큰의 예외처리 로직이 필요하다.
        Claims claims = new JwtUtils().getClaims(token);

        if (claims != null) {
            // todo: 토큰 검증로직이 여기 들어가야 한다.
            //  redis에서 userId == email 인지만 검사하는 걸로 할까?
            //  이중암호를 걸었다면, 해제로직도 여기서 있어야 한다.
            //  매번 데이터베이스를 조회하는 것보다 빠를 만한 방식을 생각해보자.
            String email = claims.get("email", String.class);
            System.out.println(email);
            // 검증 완료 후
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(claims, null, new ArrayList<>());
            return authentication;
        }
        // 기본 형태의 토큰 - UsernamePassword
        // abstractAuthenticationToken 클래스 상속받아서 직접 구현 가능.
       return null;
    }

}
