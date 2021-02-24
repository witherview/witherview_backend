//package com.witherview.interceptor;
//
//import com.witherview.account.AccountSession;
//import com.witherview.exception.BusinessException;
//import com.witherview.exception.ErrorCode;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
//
//import java.util.Map;
//
//public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request,
//                                   ServerHttpResponse response, WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) throws Exception {
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//
//            AccountSession accountSession = (AccountSession) servletRequest.getServletRequest().getSession().getAttribute("user");
//            if (accountSession == null) {
//                throw new BusinessException(ErrorCode.UNAUTHORIZED);
//            }
//        }
//        return true;
//    }
//}
