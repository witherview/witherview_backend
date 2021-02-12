package com.witherview.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RedisSubscriber {
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(String message) {
        try {
            ChatDTO.FeedBackDTO feedBackDTO = objectMapper.readValue(message, ChatDTO.FeedBackDTO.class);
            messagingTemplate.convertAndSend("/topic/feedback." + feedBackDTO.getStudyHistoryId(), message);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
