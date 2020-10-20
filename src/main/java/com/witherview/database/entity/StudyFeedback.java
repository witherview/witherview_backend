package com.witherview.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_study_feedback")
public class StudyFeedback extends CreatedBaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String feedbackMessage;

    @NotNull
    private Byte score;

    @NotNull
    private Boolean passOrFail;

    public StudyFeedback(StudyRoom studyRoom, String feedbackMessage, Byte score, Boolean passOrFail) {
        this.studyRoom = studyRoom;
        this.feedbackMessage = feedbackMessage;
        this.score = score;
        this.passOrFail = passOrFail;
    }

    protected void updateTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
}
