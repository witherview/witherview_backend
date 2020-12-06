package com.witherview.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ChatDTO {
    @Getter @Setter
    public static class ChatMessageDTO {
        private String id = UUID.randomUUID().toString(); // message id
        private Long roomId; // 방 번호
        private String sender; // 메세지 보낸사람
        private String contents; // 메세지
    }

    @Getter @Setter
    public static class ChatFeedBackDTO {
        private String id = UUID.randomUUID().toString(); // message id
        private Long roomId; // 방 번호
        private String sender; // 피드백 보낸사람
        private String target; // 피드백 받는사람
        private String feedbacks; // 피드백
    }
}
