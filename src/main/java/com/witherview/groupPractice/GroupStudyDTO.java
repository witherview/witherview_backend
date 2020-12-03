package com.witherview.groupPractice;

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
    @Getter @Setter @Builder
    public static class StudyCreateDTO {
        @NotBlank(message = "방 제목은 반드시 입력해야 합니다.")
        @Length(min = 1, message = "방 제목은 1자 이상이어야 합니다.")
        @Length(max = 20, message = "방 제목은 20자 이하여야 합니다.")
        private String title;

        @NotBlank(message = "방 설명은 반드시 입력해야 합니다.")
        @Length(min = 1, message = "방 설명은 1자 이상이어야 합니다.")
        @Length(max = 100, message = "방 설명은 100자 이하여야 합니다.")
        private String description;

        private String industry;
        private String job;

        private LocalDate date;
        private LocalTime time;

        public StudyRoom toEntity() {
            return StudyRoom.builder()
                    .title(title)
                    .description(description)
                    .industry(industry)
                    .job(job)
                    .date(date)
                    .time(time)
                    .build();
        }
    }

    @Getter @Setter @Builder
    public static class StudyUpdateDTO {
        @NotNull(message = "방 아이디는 반드시 입력해야 합니다.")
        private Long id;

        @NotBlank(message = "방 제목은 반드시 입력해야 합니다.")
        private String title;

        @NotBlank(message = "방 설명은 반드시 입력해야 합니다.")
        private String description;

        private String industry;
        private String job;

        private LocalDate date;
        private LocalTime time;
    }

    @Getter @Setter @Builder
    public static class StudyFeedBackDTO {
        @NotNull(message = "방 아이디는 반드시 입력해야 합니다.")
        private Long id;

        @NotNull(message = "타겟 유저아이디는 반드시 입력해야 합니다.")
        private Long targetUser;

        @NotNull(message = "점수는 반드시 입력해야 합니다.")
        private Byte score;

        @NotNull(message = "합격 여부는 반드시 입력해야 합니다.")
        private Boolean passOrFail;
    }

    @Getter @Setter
    public static class StudyJoinDTO {
        @NotNull(message = "방 아이디는 반드시 입력해야 합니다.")
        private Long id;
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
    }

    @Getter @Setter
    public static class ParticipantDTO {
        private String email;
        private String name;
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
