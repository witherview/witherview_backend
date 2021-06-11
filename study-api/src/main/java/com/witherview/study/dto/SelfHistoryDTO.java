package com.witherview.study.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class SelfHistoryDTO {
    @Getter @Setter
    public static class SelfHistoryRequestDTO {
        @NotNull(message = "질문 리스트 아이디는 반드시 입력해야 합니다.")
        private Long questionListId;
    }

    @Getter @Setter
    public static class SelfHistoryDefaultResponseDTO {
        private Long id;
    }

    @Getter @Setter
    public static class SelfHistoryResponseDTO {
        private Long id;
        private Long questionListId;
        private String historyTitle;
        private String savedLocation;
        private LocalDateTime createdAt;
    }
}
