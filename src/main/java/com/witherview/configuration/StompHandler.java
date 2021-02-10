package com.witherview.configuration;

import com.witherview.groupPractice.GroupStudy.GroupStudyService;
import com.witherview.groupPractice.history.StudyHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final GroupStudyService groupStudyService;
    private final StudyHistoryService studyHistoryService;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // websocket 연결 후 subscribe 시 존재하는 방 구독하는지 체크
        if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
            System.out.println(message.getHeaders());
            String type = message.getHeaders().get("simpDestination").toString().split("/")[2];
            // todo: 방이 없거나, string이 아닐 경우의 exception은 필요하지 않나?
            //  현재는  exception : connection refused되는 문제 있음
            String room = message.getHeaders().get("simpDestination").toString().split("\\.")[1];

            Long id = Long.parseLong(room);
            try {
                if(type.equals("feedback")) studyHistoryService.findStudyHistory(id);
                else groupStudyService.findRoom(id);
            } catch (Exception e) {
                throw new MessagingException(e.getMessage());
            }
        }
        return message;
    }
}