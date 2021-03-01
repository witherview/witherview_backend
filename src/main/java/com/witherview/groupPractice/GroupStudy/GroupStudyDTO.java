package com.witherview.groupPractice.GroupStudy;

import com.witherview.database.entity.StudyRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class GroupStudyDTO {
    @Getter @Setter
    public static class StudyCreateDTO {
        @NotBlank(message = "방 제목은 반드시 입력해야 합니다.")
        @Length(min = 1, message = "방 제목은 1자 이상이어야 합니다.")
        @Length(max = 20, message = "방 제목은 20자 이하여야 합니다.")
        private String title;

        @NotBlank(message = "방 설명은 반드시 입력해야 합니다.")
        @Length(min = 1, message = "방 설명은 1자 이상이어야 합니다.")
        @Length(max = 100, message = "방 설명은 100자 이하여야 합니다.")
        private String description;

        private String category;
        private String industry;
        private String job;

        private String date;
        private String time;

    }

    @Getter @Setter
    public static class StudyUpdateDTO {
        @NotNull(message = "방 아이디는 반드시 입력해야 합니다.")
        private Long id;

        @NotBlank(message = "방 제목은 반드시 입력해야 합니다.")
        private String title;

        @NotBlank(message = "방 설명은 반드시 입력해야 합니다.")
        private String description;

        private String category;
        private String industry;
        private String job;

        private String date;
        private String time;
    }

    @Getter @Setter
    public static class StudyFeedBackDTO {
        @NotNull(message = " 아이디는 반드시 입력해야 합니다.")
        private Long studyRoomId;

        @NotNull(message = "스터디 연습내역 아이디는 반드시 입력해야 합니다.")
        private Long historyId;

        @NotNull(message = "타겟 유저아이디는 반드시 입력해야 합니다.")
        private String receivedUser; // 이메일?

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
    public static class ResponseDTO {
        private Long id;
        private String title;
        private String description;
        private String category;
        private String industry;
        private String job;
        private LocalDate date;
        private LocalTime time;
        private Integer nowUserCnt;
        private Integer maxUserCnt;
    }

    @Getter @Setter
    public static class ParticipantDTO {
        private Long id;
        private String email;
        private String name;
        private Long groupPracticeCnt;
        private Byte reliability;
        private Boolean isHost;
    }

    @Getter @Setter
    public static class FeedBackResponseDTO {
        private Long id;
        private Long targetUserId;
        private Byte score;
        private Boolean passOrFail;
    }

    @Getter @Setter
    public static class DeleteResponseDTO {
        private Long id;
    }
}
