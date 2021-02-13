package com.witherview.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter @NoArgsConstructor @Builder @AllArgsConstructor
@Document
@CompoundIndex(def = "{'writtenUserId': 1 , 'studyHistoryId': -1, 'targetUserId': 1}")
public class FeedBackChat {
    @Id
    private String id;
    // compoundKey
    @NotNull(message = "피드백 보낸사람 아이디는 반드시 입력해야 합니다.")
    private Long targetUserId;
    @NotNull(message = "피드백 받는사람 아이디는 반드시 입력해야 합니다.")
    private Long writtenUserId;
    @NotBlank(message = "피드백 메세지는 반드시 입력해야 합니다.")
    private Long studyHistoryId;
    
//    private String writtenUserName;
    @NotBlank
    private String message;

    private Instant createdAt; // 영상 시작하고 몇 분에 달린 comment인지?
    private Instant timestamp;

}
