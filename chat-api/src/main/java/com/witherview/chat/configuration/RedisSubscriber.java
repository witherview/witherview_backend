package com.witherview.chat.configuration;

import com.witherview.chat.dto.ChatDTO.FeedBackDTO;
import com.witherview.chat.dto.ChatDTO.MessageDTO;
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

    public void sendFeedback(String message) {
        try {
            FeedBackDTO feedBackDTO = objectMapper.readValue(message, FeedBackDTO.class);
            messagingTemplate.convertAndSend("/sub/feedback." + feedBackDTO.getStudyHistoryId(), message);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    public void sendChat(String message) {
        try {
            MessageDTO messageDTO = objectMapper.readValue(message, MessageDTO.class);
            messagingTemplate.convertAndSend("/sub/room." + messageDTO.getStudyRoomId(), message);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
