package com.witherview.groupPractice.history;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class StudyHistoryDTO {
    @Getter
    @Setter
    public static class HistoryCreatedResponseDTO {
        private Long id;
    }

    @Getter @Setter
    public static class HistoryResponseDTO {
        private Long id;
        private Long studyRoom;
        private String savedLocation;
        private LocalDateTime createdAt;
    }

    @Getter @Setter
    public static class VideoSaveResponseDTO {
        private Long id;
        private String savedLocation;
    }
}
