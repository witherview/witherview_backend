package com.witherview.study.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

public class GroupStudyDTO {
    @Getter @Setter
    public static class StudyCreateDTO {
        @NotBlank(message = "방 제목은 반드시 입력해야 합니다.")
        @Length(min = 1, message = "방 제목은 1자 이상이어야 합니다.")
        @Length(max = 50, message = "방 제목은 50자 이하여야 합니다.")
        private String title;

        @NotBlank(message = "방 설명은 반드시 입력해야 합니다.")
        @Length(min = 1, message = "방 설명은 1자 이상이어야 합니다.")
        @Length(max = 100, message = "방 설명은 100자 이하여야 합니다.")
        private String description;

        private String industry;
        private String job;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate date;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        private LocalTime time;
    }

    @Getter @Setter
    public static class StudyUpdateDTO {
        @NotNull(message = "방 아이디는 반드시 입력해야 합니다.")
        private Long id;

        @NotBlank(message = "방 제목은 반드시 입력해야 합니다.")
        private String title;

        @NotBlank(message = "방 설명은 반드시 입력해야 합니다.")
        private String description;

        private String industry;
        private String job;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate date;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        private LocalTime time;
    }

    @Getter @Setter
    public static class StudyFeedBackDTO {
        @NotNull(message = " 아이디는 반드시 입력해야 합니다.")
        private Long studyRoomId;

        @NotNull(message = "스터디 연습내역 아이디는 반드시 입력해야 합니다.")
        private Long studyHistoryId;

        @NotNull(message = "타겟 유저아이디는 반드시 입력해야 합니다.")
        private String receivedUser;

        @NotNull(message = "점수는 반드시 입력해야 합니다.")
        private Byte score;

        @NotNull(message = "합격 여부는 반드시 입력해야 합니다.")
        private Boolean passOrFail;
    }

    @Getter @Setter
    public static class StudyRequestDTO {
        @NotNull(message = "방 아이디는 반드시 입력해야 합니다.")
        private Long id;
    }

    @Getter @Setter
    public static class StudyHostDTO {
        @NotBlank(message = "권한을 넘겨줄 호스트 아이디는 반드시 입력해야 합니다.")
        private String newHostId;
    }

    @Getter @Setter
    public static class ResponseDTO {
        private Long id;
        private String title;
        private String description;
        private String industry;
        private String job;
        private LocalDate date;
        private LocalTime time;
        private Integer nowUserCnt;
        private Integer maxUserCnt;
    }

    @Getter @Setter
    public static class ParticipantDTO {
        private String id;
        private String email;
        private String name;
        private Long groupPracticeCnt;
        private Byte reliability;
        private Boolean isHost;
    }

    @Getter @Setter
    public static class FeedBackResponseDTO {
        private Long id;
        private String receivedUser;
        private Byte score;
        private Boolean passOrFail;
    }

    @Getter @Setter
    public static class DeleteResponseDTO {
        private Long id;
    }

    @Getter @Setter
    public static class UserRankResponseDTO {
        private String id;
        private String email;
        private String name;
        private Byte reliability;
        private String profileImg;
    }
}
