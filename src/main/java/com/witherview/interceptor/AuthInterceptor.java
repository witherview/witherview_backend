package com.witherview.interceptor;

import com.witherview.account.AccountSession;
import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("user");
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) { // CORS..
            return true;
        }
        if (accountSession == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return true;
    }
}
