package com.witherview.interceptor;

import com.witherview.account.AccountSession;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("user");
        if (accountSession == null) return false;

        int emailStartIdx = 8;
        String fromEmailToEnd = request.getRequestURI().substring(emailStartIdx);
        int slashIdx = fromEmailToEnd.indexOf("/");
        if (slashIdx == -1) return false;

        String requestEmail = fromEmailToEnd.substring(0, slashIdx);
        return accountSession.getEmail().equals(requestEmail);
    }
}
