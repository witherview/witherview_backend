package com.witherview.configuration;

import com.witherview.account.exception.InvalidJwtTokenException;
import com.witherview.exception.ErrorCode;
import com.witherview.groupPractice.GroupStudy.GroupStudyService;
import com.witherview.groupPractice.history.StudyHistoryService;
import com.witherview.configuration.authentication.JWSAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;
import org.springframework.security.messaging.access.intercept.MessageSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    @Qualifier("websocket")
    private final AuthenticationManager authenticationManager;

    private final GroupStudyService groupStudyService;
    private final StudyHistoryService studyHistoryService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand() || StompCommand.SEND == accessor.getCommand()) {
            // 토큰이 헤더에 있는지 검증
            Optional.ofNullable(accessor.getNativeHeader("Authorization"))
                    .ifPresentOrElse(auth -> {
                        var bearerToken = auth.get(0).replace("Bearer ", "");
//                        System.out.println("Received Bearer Token : " + bearerToken);
                        var jwsAuthToken = new JWSAuthenticationToken(bearerToken);
                        Authentication token = authenticationManager.authenticate(jwsAuthToken);
                        if (!token.isAuthenticated()) {
                            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
                        }
                        SecurityContextHolder.getContext().setAuthentication(token);
                        accessor.setUser(token);
                    }, () -> {
                        throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
                    }
            );
        }
        // websocket 연결 후 subscribe 시 존재하는 방 구독하는지 체크
        if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
            System.out.println(message.getHeaders());
            String type = message.getHeaders().get("simpDestination").toString()
                    .split("/")[2] // return type.id
                    .split("\\.")[0]; // return type

            String room = message.getHeaders().get("simpDestination").toString().split("\\.")[1];
            Long id = Long.parseLong(room);
            try {
                if (type.equals("feedback")) {
                    studyHistoryService.findStudyHistory(id);
                }
                else groupStudyService.findRoom(id);
            } catch (Exception e) {
                throw new MessagingException(e.getMessage());
            }
        }
        System.out.println("stompHandler : " + message);
        return message;
    }
}