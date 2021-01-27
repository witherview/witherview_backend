package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_group_study")
public class StudyHistory extends CreatedBaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private Long studyRoom;

    private String savedLocation;

    @OneToMany(mappedBy = "studyHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyFeedback> studyFeedbacks = new ArrayList<>();

    @Builder
    public StudyHistory(Long studyRoom) {
        this.studyRoom = studyRoom;
    }

    protected void updateUser(User user) {
        this.user = user;
    }

    public void updateSavedLocation(String savedLocation) {
        this.savedLocation = savedLocation;
    }

    public void addStudyFeedBack(StudyFeedback studyFeedback) {
        studyFeedback.updateStudyHistory(this);
        this.studyFeedbacks.add(studyFeedback);
    }
}
