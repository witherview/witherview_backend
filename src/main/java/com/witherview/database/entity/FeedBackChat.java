package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_feedback_chat")
public class FeedBackChat {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "written_user_id", nullable = false)
    private User writtenUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_history_id", nullable = false)
    private StudyHistory studyHistory;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @NotNull @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public FeedBackChat(StudyHistory studyHistory, User writtenUser, User targetUser, String message, LocalDateTime createdAt) {
        this.studyHistory = studyHistory;
        this.writtenUser = writtenUser;
        this.targetUser = targetUser;
        this.message = message;
        this.createdAt = createdAt;
    }
}
