package com.witherview.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ChatDTO {
    @Getter @Setter @ToString
    /*
    {
        "studyRoomId" : 17,
        "userName" : "yeeun",
        "message" : "hihihh"
    }
    * */
    public static class MessageDTO {
        private String id; // message id - mongoDB에 저장할 경우 따로 uuid 필요 없음

        @NotNull(message = "방id는 반드시 입력해야 합니다.")
        private Long studyRoomId; // 방 id

        private String userId; // 메세지 보낸사람 아이디

        @NotNull(message = "채팅 보낸사람 이름은 반드시 입력해야 합니다.")
        private String userName; // 메세지 보낸사람 이름

        @NotBlank(message = "채팅 메세지는 반드시 입력해야 합니다.")
        private String message; // 메세지

        private LocalDateTime timestamp = LocalDateTime.now();
    }

    @Getter @Setter @ToString
    /*
    {
        "studyHistoryId" : 19,
        "receivedUserId" : "adfqewrasdfadf",
        "message" : "feedback"
    }
    * */
    public static class FeedBackDTO {
        private String id;

        @NotNull(message = "스터디 연습내역 아이디는 반드시 입력해야 합니다.")
        private Long studyHistoryId; // 방 번호

        private String sendUserId; // 피드백 보낸사람 id

        @NotNull(message = "피드백 보낸사람 이름은 반드시 입력해야 합니다.")
        private String sendUserName; // 피드백 보낸사람 - 프론트에 표시될 이름

        @NotNull(message = "피드백 받는사람 아이디는 반드시 입력해야 합니다.")
        private String receivedUserId; // 피드백 받는사람 id

        @NotNull(message = "피드백 받는사람 아이디는 반드시 입력해야 합니다.")
        private String receivedUserName; // 피드백 받는사람 - 프론트에 표시될 이름

        @NotBlank(message = "피드백 메세지는 반드시 입력해야 합니다.")
        private String message; // 피드백

        private LocalTime createdAt;

        private LocalDateTime timestamp = LocalDateTime.now();
    }

    @Getter @Setter
    public static class SaveDTO {
        @NotNull(message = "스터디 연습내역 아이디는 반드시 입력해야 합니다.")
        private Long studyHistoryId;
    }
}
