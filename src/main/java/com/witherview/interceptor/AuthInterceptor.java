package com.witherview.interceptor;

import com.witherview.account.exception.InvalidJwtTokenException;
import com.witherview.constant.SecurityConstant;
import com.witherview.exception.ErrorCode;
import com.witherview.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 헤더에 auth 값이 없는 경우.
        String header = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);
        System.out.println("interceptor : " + header);
        if (header == null || !header.startsWith(SecurityConstant.TOKEN_PREFIX))
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        var token = header.substring(SecurityConstant.TOKEN_PREFIX.length());
        Claims claims = new JwtUtils().getClaims(token);
        if (claims != null)
            return true;

        return false;
    }
}
