package com.witherview.study.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class SelfHistoryDTO {
    @Getter @Setter
    public static class SelfHistoryCreateRequestDTO {
        @NotNull(message = "질문 리스트 아이디는 반드시 입력해야 합니다.")
        private Long questionListId;
    }

    @Getter @Setter
    public static class SelfHistoryUpdateRequestDTO {
        @NotNull(message = "연습내역 아이디는 반드시 입력해야 합니다.")
        private Long historyId;
        private String historyTitle;
    }

    @Getter @Setter
    public static class SelfHistoryIdResponseDTO {
        private Long id;
    }

    @Getter @Setter
    public static class ResponseDTO {
        private Long id;
        private Long questionListId;
        private String historyTitle;
        private String savedLocation;
        private String thumbnail;
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
