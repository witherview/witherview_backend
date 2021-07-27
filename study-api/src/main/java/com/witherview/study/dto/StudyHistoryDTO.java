package com.witherview.study.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
        private String thumbnail;
        private String videoInfo;
    }
}
