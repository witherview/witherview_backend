package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class SelfQuestionListDTO {

    @Getter @Setter @Builder
    public static class SaveDTO {
        @NotBlank(message = "질문리스트 제목은 반드시 입력해야 합니다.")
        private String title;

        @NotBlank(message = "기업 이름은 반드시 입력해야 합니다.")
        private String enterprise;

        @NotBlank(message = "직무 이름은 반드시 입력해야 합니다.")
        private String job;

        @NotNull(message = "순서는 반드시 입력해야 합니다.")
        private Integer order;

        public QuestionList toEntity() {
            return QuestionList.builder()
                    .title(title)
                    .enterprise(enterprise)
                    .job(job)
                    .order(order)
                    .build();
        }
    }

    @Getter @Setter @Builder
    public static class UpdateDTO {
        @NotBlank(message = "질문리스트 제목은 반드시 입력해야 합니다.")
        private String title;

        @NotBlank(message = "기업 이름은 반드시 입력해야 합니다.")
        private String enterprise;

        @NotBlank(message = "직무 이름은 반드시 입력해야 합니다.")
        private String job;

        private Integer order;
    }

    @Getter @Setter
    public static class ResponseDTO {
        private Long id;
        private String title;
        private String enterprise;
        private String job;
        private Integer order;
    }

    @Getter @Setter
    public static class DeleteResponseDTO {
        private Long id;
    }
}