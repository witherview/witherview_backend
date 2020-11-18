package com.witherview.selfPractice.history;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class SelfHistoryDTO {

    @Getter @Setter
    public static class SelfHistorySaveDTO {
        @NotNull(message = "질문리스트 아이디는 반드시 입력해야 합니다.")
        private Long questionListId;
    }

    public static class SelfHistorySaveResponseDTO {
        private Long id;
    }
}
