package com.witherview.signal;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class SignalController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/signal")
    public void signal(SignalDTO.MessageDTO message) {
        messagingTemplate.convertAndSend("/sub/signal/" + message.getRoomId(), message);
    }
}
