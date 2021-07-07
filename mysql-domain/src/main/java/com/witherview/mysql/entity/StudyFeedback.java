package com.witherview.mysql.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_study_feedback")
public class StudyFeedback extends CreatedBaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String receivedUser; // 받은 사람 (조회할 때 사용할 필드)

    private String sendUser; // 보낸 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_history_id", nullable = false)
    private StudyHistory studyHistory;

    @NotNull
    private Byte score;

    @NotNull
    private Boolean passOrFail;

    @Builder
    public StudyFeedback(String receivedUser, String sendUser,
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
