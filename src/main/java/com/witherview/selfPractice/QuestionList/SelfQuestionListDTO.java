package com.witherview.selfPractice.QuestionList;

import com.witherview.database.entity.QuestionList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class SelfQuestionListDTO {
    private static final ModelMapper modelMapper = new ModelMapper();

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

        public static ResponseDTO of(QuestionList questionList) {
            ResponseDTO responseDTO = modelMapper.map(questionList, ResponseDTO.class);
            return responseDTO;
        }

        public static List<ResponseDTO> of(List<QuestionList> questionList) {
            return questionList.stream()
                    .map(ResponseDTO::of)
                    .collect(Collectors.toList());
        }
    }

    @Getter @Setter
    public static class DeleteResponseDTO {
        private Long id;

        public static DeleteResponseDTO of(QuestionList questionList) {
            DeleteResponseDTO deleteResponseDTO = modelMapper.map(questionList, DeleteResponseDTO.class);
            return deleteResponseDTO;
        }
    }
}