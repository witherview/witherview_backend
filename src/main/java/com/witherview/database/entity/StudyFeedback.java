package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_study_feedback")
public class StudyFeedback extends CreatedBaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User receivedUser; // 받은 사람람 (조회할 때 사용할 필드)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "written_user_id", nullable = false)
    private User sendUser; // 보낸 사

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_history_id", nullable = false)
    private StudyHistory studyHistory;

    @NotNull
    private Byte score;

    @NotNull
    private Boolean passOrFail;

    @Builder
    public StudyFeedback(User receivedUser, User sendUser,
                         Byte score, Boolean passOrFail) {
        this.receivedUser = receivedUser;
        this.sendUser = sendUser;
        this.score = score;
        this.passOrFail = passOrFail;
    }

    protected void updateStudyHistory(StudyHistory studyHistory) {
        this.studyHistory = studyHistory;
    }
}
