package com.witherview.signal;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class SignalDTO {
    @Getter @Setter
    public static class MessageDTO {
        private String id = UUID.randomUUID().toString(); // message id
        private Long roomId; // 방 번호
        private String type; // 메세지 타입
        private String sender; // 메세지 보낸사람
        private String contents; // 메세지
    }
}
