package com.witherview.selfPractice.Question;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class SelfQuestionDTO {

    @Getter @Setter
    public static class QuestionSaveDTO {
        @NotNull(message = "리스트 아이디는 반드시 입력해야 합니다.")
        private Long listId;

        @NotNull(message = "질문은 반드시 입력해야 합니다.")
        private List<QuestionDTO> questions;
    }

    @Getter @Setter
    public static class QuestionDTO {

        @NotBlank(message = "질문은 반드시 입력해야 합니다.")
        private String question;

        private String answer;

        @NotNull(message = "순서는 반드시 입력해야 합니다.")
        private Integer order;
    }

    @Getter @Setter
    public static class QuestionUpdateDTO {
        @NotNull(message = "질문 id는 반드시 입력해야 합니다.")
        private Long id;

        @NotBlank(message = "질문은 반드시 입력해야 합니다.")
        private String question;

        private String answer;

        @NotNull(message = "순서는 반드시 입력해야 합니다.")
        private Integer order;
    }

    @Getter @Setter
    public static class ResponseDTO {
        private Long id;
        private String question;
        private String answer;
        private Integer order;
        private LocalDateTime modifiedAt;
    }

    @Getter @Setter
    public static class DeleteResponseDTO {
        private Long id;
    }
}
