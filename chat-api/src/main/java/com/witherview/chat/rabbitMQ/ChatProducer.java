package com.witherview.chat.rabbitMQ;

import com.witherview.chat.dto.ChatDTO.FeedBackDTO;
import com.witherview.chat.dto.ChatDTO.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private static final String EXCHANGE_NAME = "topic";

    @Autowired
    public ChatProducer(RabbitTemplate rabbitTemplate,
                        ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendChat(MessageDTO message) throws JsonProcessingException {
        System.out.println("chat " + message.getMessage());
        String value = objectMapper.writeValueAsString(message);
        this.rabbitTemplate.convertAndSend(EXCHANGE_NAME, "chat."+message.getStudyRoomId(), value);
    }

    public void sendFeedback(FeedBackDTO message) throws JsonProcessingException {
        System.out.println("feedback " + message.getMessage());
        String value = objectMapper.writeValueAsString(message);
        this.rabbitTemplate.convertAndSend(EXCHANGE_NAME, "feedback."+message.getStudyHistoryId(), value);
    }
}
