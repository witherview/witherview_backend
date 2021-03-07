package com.witherview.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.witherview.WitherviewApplicationContext;
import com.witherview.account.AccountDTO;
import com.witherview.account.AccountService;
import com.witherview.account.exception.InvalidLoginException;
import com.witherview.constant.SecurityConstant;
import com.witherview.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Authentication 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            var credentials = new ObjectMapper().readValue(request.getInputStream(), AccountDTO.LoginDTO.class);
            // loadUserByUsername 실행하는 영역
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(), // 아이디
                            credentials.getPassword(), // 패스워드
                            new ArrayList<>() // 권한
                    )
            );
        } catch (IOException e) {
            throw new InvalidLoginException();
        }
    }

    // 로그인 성공 시 호출
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // accountService를 호출한다.
        AccountService accountService = (AccountService) WitherviewApplicationContext.getBean("accountService");

        // email / userId값을 토큰에 넣어 사용한다.
        String email = ((User) authResult.getPrincipal()).getUsername();
        var userId = accountService.findUserByEmail(email).getId();
        String token = new JwtUtils().createToken(email, userId);
        Map<String, String> data = new HashMap<>();
        data.put("accessToken", token);
        String json = new ObjectMapper().writeValueAsString(data);
//        response.addHeader(SecurityConstant.AUTHORIZATION_HEADER, SecurityConstant.TOKEN_PREFIX + token);
        response.getWriter().write(json);
        log.info("User: " + email + " login Accepted. Token : " + token + " created.");
    }
}
