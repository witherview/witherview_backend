package com.witherview.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_group_study_video")
public class StudyVideo {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String savedLocation;

    public void updateSavedLocation(String savedLocation) {
        this.savedLocation = savedLocation;
    }

    protected void updateUser(User user) {
        this.user = user;
    }
}
