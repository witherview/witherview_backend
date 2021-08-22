package com.witherview.mysql.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_user", indexes = @Index(name = "email", columnList = "email"))
public class User {

    @Id
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String encryptedPassword;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String mainIndustry;

    private String subIndustry;

    private String mainJob;

    private String subJob;

    private String profileImg;

    private String phoneNumber;

    private String passwordResetToken;

    @ColumnDefault("0")
    private Long selfPracticeCnt = 0L;

    @ColumnDefault("0")
    private Long groupPracticeCnt = 0L;

    @ColumnDefault("0")
    private Byte reliability = 70;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRoomParticipant> participatedStudyRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyHistory> studyHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfHistory> selfHistories = new ArrayList<>();

    @Builder
    public User(String email, String encryptedPassword, String name,
                String mainIndustry, String subIndustry, String mainJob, String subJob, String phoneNumber) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.mainIndustry = mainIndustry;
        this.subIndustry = subIndustry;
        this.mainJob = mainJob;
        this.subJob = subJob;
        this.phoneNumber = phoneNumber;
    }

    public void increaseSelfPracticeCnt() {
        this.selfPracticeCnt += 1;
    }

    public void increaseGroupPracticeCnt() {
        this.groupPracticeCnt += 1;
    }

    public void addStudyHistory(StudyHistory studyHistory) {
        studyHistory.updateUser(this);
        this.studyHistories.add(studyHistory);
    }

    public void addParticipatedRoom(StudyRoomParticipant participatedStudyRoom) {
        this.participatedStudyRooms.add(participatedStudyRoom);
    }

    public void addSelfHistory(SelfHistory selfHistory) {
        selfHistory.updateUser(this);
        this.selfHistories.add(selfHistory);
    }

    public void update(String name, String mainIndustry, String subIndustry,
                       String mainJob, String subJob, String phoneNumber) {
        this.name = name;
        this.mainIndustry = mainIndustry;
        this.subIndustry = subIndustry;
        this.mainJob = mainJob;
        this.subJob = subJob;
        this.phoneNumber = phoneNumber;
    }

    public void uploadImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
