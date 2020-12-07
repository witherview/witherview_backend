package com.witherview.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat")
    public void message(ChatDTO.ChatMessageDTO message) {
        messagingTemplate.convertAndSend("/sub/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/feedback")
    public void feedback(ChatDTO.ChatFeedBackDTO feedback) {
        messagingTemplate.convertAndSend("/sub/feedback" + feedback.getRoomId(), feedback);
    }
}
