package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter @NoArgsConstructor @Builder
@Document
@CompoundIndex(def = "{'writtenUserId': 1 , 'studyHistoryId': -1, 'targetUserId': 1}")
public class FeedBackChat {
    @Id
    private String id;
    // compoundKey
    private Long targetUserId;
    private Long writtenUserId;
    private Long studyHistoryId;

    private String targetUserName;
    private String writtenUserName;
    @NotBlank
    private String message;

    private LocalTime createdAt; // 영상 시작하고 몇 분에 달린 comment인지.
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

}
