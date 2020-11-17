package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class SelfQuestionListDTO {

    @Getter @Setter @Builder
    public static class QuestionListSaveDTO {
        @NotBlank(message = "질문리스트 제목은 반드시 입력해야 합니다.")
        private String title;

        @NotBlank(message = "기업 이름은 반드시 입력해야 합니다.")
        private String enterprise;

        @NotBlank(message = "직무 이름은 반드시 입력해야 합니다.")
        private String job;

        public QuestionList toEntity() {
            return QuestionList.builder()
                    .title(title)
                    .enterprise(enterprise)
                    .job(job)
                    .build();
        }
    }

    @Getter @Setter @Builder
    public static class QuestionListUpdateDTO {
        @NotNull(message = "질문리스트 아이디는 반드시 입력해야 합니다.")
        private Long id;

        @NotBlank(message = "질문리스트 제목은 반드시 입력해야 합니다.")
        private String title;

        @NotBlank(message = "기업 이름은 반드시 입력해야 합니다.")
        private String enterprise;

        @NotBlank(message = "직무 이름은 반드시 입력해야 합니다.")
        private String job;
    }

    @Getter @Setter
    public static class ResponseDTO {
        private Long id;
        private String title;
        private String enterprise;
        private String job;
    }

    @Getter @Setter
    public static class DeleteResponseDTO {
        private Long id;
    }
}