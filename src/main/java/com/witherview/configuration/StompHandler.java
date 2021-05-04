package com.witherview.configuration;

import com.witherview.account.exception.InvalidJwtTokenException;
import com.witherview.exception.ErrorCode;
import com.witherview.groupPractice.GroupStudy.GroupStudyService;
import com.witherview.groupPractice.history.StudyHistoryService;
import com.witherview.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final GroupStudyService groupStudyService;
    private final StudyHistoryService studyHistoryService;
    private final JwtUtils jwtUtils = new JwtUtils();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

//        if(StompCommand.CONNECT == accessor.getCommand() || StompCommand.SEND == accessor.getCommand()) {
//            // 토큰 검증
//            try {
//                String header = accessor.getFirstNativeHeader("Authorization");
//                if(!jwtUtils.verifyToken(header)) throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
//            } catch (Exception e) {
//                throw new MessagingException(e.getMessage());
//            }
//        }
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