package com.witherview.mysql.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Setter
    private String thumbnail;
    @Setter
    private String videoInfo;

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
