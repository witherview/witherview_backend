package com.witherview.chat;

import com.witherview.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChatDTO {
    @Getter @Setter @ToString
    /*
    {
        "studyRoomId" : 17,
        "userId" : 1,
        "userName" : "yeeun",
        "message" : "hihihh"
    }
    * */
    public static class MessageDTO {
        private String id; // message id - mongoDB에 저장할 경우 따로 uuid 필요 없음

        @NotNull(message = "방 id는 반드시 입력해야 합니다.")
        private Long studyRoomId; // 방 id

        @NotNull(message = "채팅 보낸사람 아이디는 반드시 입력해야 합니다.")
        private Long userId; // 메세지 보낸사람 아이디

        @NotNull(message = "채팅 보낸사람 이름은 반드시 입력해야 합니다.")
        private String userName; // 메세지 보낸사람 이름

        @NotBlank(message = "채팅 메세지는 반드시 입력해야 합니다.")
        private String message; // 메세지
        private String timestamp = StringUtils.getCurrentDateTimeStamp();
    }

    @Getter @Setter @ToString
    /*
    {
        "studyHistoryId" : 19,
        "sendUserId" : 1,
        "receivedUserId" : 1,
        "message" : "feedback"
    }
    * */
    public static class FeedBackDTO {
        private String id;

        @NotNull(message = "스터디 연습내역 아이디는 반드시 입력해야 합니다.")
        private Long studyHistoryId; // 방 번호

        @NotNull(message = "피드백 보낸사람 아이디는 반드시 입력해야 합니다.")
        private Long sendUserId; // 피드백 보낸사람

        @NotNull(message = "피드백 받는사람 아이디는 반드시 입력해야 합니다.")
        private Long receivedUserId; // 피드백 받는사람

        @NotBlank(message = "피드백 메세지는 반드시 입력해야 합니다.")
        private String message; // 피드백

        private String createdAt;
        // todo: 이동건. createdAt 쓰임새가 명확해지면 제거해야 할 수도 있는 값.
        private String timestamp = StringUtils.getCurrentDateTimeStamp();
    }

    @Getter @Setter
    public static class SaveDTO {
        @NotNull(message = "스터디 연습내역 아이디는 반드시 입력해야 합니다.")
        private Long studyHistoryId;
    }
}
