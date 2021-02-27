package com.witherview.database.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Document
// 방 id순 오름차순 정렬 해당 방에서는 시간순서로 정리.
@CompoundIndex(def = "{'studyRoomId' : 1, 'timestamp' : 1}")
public class Chat {

    @Id
    private String id;
    @NotNull
    private Long userId;

    @NotNull
    private String userName;

    @NotNull
    private Long studyRoomId;

    @NotNull(message = "메시지는 반드시 입력해야 합니다.")
    private String message;

    private LocalDateTime timestamp;
}
