package com.witherview.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.witherview.utils.StringUtils;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class ChatDTO {
    @Getter @Setter @ToString
    public static class MessageDTO {
        private String id; // message id - mongoDB에 저장할 경우 따로 uuid 필요 없음
        private Long roomId; // 방 번호
        private String type; // 메세지 타입
        private String sender; // 메세지 보낸사람
        private String contents; // 메세지
        private String createdAt; // 영상 어느 시점에 생성된 메시지인지? : HH:mm:ss 형태로 리턴받아야 함
        private String timestamp = StringUtils.getCurrentDateTimeStamp();
    }

    @Getter @Setter @ToString
    public static class FeedBackDTO {
        private String id;

        @NotNull(message = "스터디 연습내역 아이디는 반드시 입력해야 합니다.")
        private Long studyHistoryId; // 방 번호

        @NotNull(message = "피드백 보낸사람 아이디는 반드시 입력해야 합니다.")
        @JsonIgnore
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
