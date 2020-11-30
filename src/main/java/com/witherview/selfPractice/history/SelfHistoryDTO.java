package com.witherview.selfPractice.history;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class SelfHistoryDTO {
    @Getter @Setter
    public static class SelfHistorySaveResponseDTO {
        private Long id;
    }

    @Getter @Setter
    public static class SelfHistoryResponseDTO {
        private Long id;
        private Long questionListId;
        private String questionListEnterprise;
        private String questionListJob;
        private String savedLocation;
        private LocalDateTime createdAt;
    }
}
