package com.witherview.database.entity;

import com.witherview.groupPractice.exception.AlreadyFullStudyRoom;
import com.witherview.groupPractice.exception.EmptyStudyRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_study_room")
public class StudyRoom {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User host; // host와 participant로 구분되어야 할 이유는? User - studyRoom N:M 관계면 충분하지 않나?

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Integer nowUserCnt;

    @NotNull
    @Column(nullable = false)
    private Integer maxUserCnt;

    @ColumnDefault("'자유_기타'")
    private String category;

    @ColumnDefault("'무관'")
    private String industry;

    @ColumnDefault("'무관'")
    private String job;

    @Column(columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(columnDefinition = "TIME")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime time;

    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRoomParticipant> studyRoomParticipants = new ArrayList<>();

    @Builder
    public StudyRoom(String title, String description,
                     String category, String industry, String job,
                     LocalDate date, LocalTime time) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.industry = industry;
        this.job = job;
        this.date = date;
        this.time = time;
        this.nowUserCnt = 0;
        this.maxUserCnt = 2;
    }

    protected void updateHost(User host) {
        this.host = host;
    }

    public void addParticipants(StudyRoomParticipant studyRoomParticipant) {
        this.studyRoomParticipants.add(studyRoomParticipant);
    }

    public void update(String title, String description,
                       String industry, String job,
                       LocalDate date, LocalTime time) {
        this.title = title;
        this.description = description;
        this.industry = industry;
        this.job = job;
        this.date = date;
        this.time = time;
    }

    public void increaseNowUserCnt() {
        if(this.nowUserCnt + 1 > this.maxUserCnt) throw new AlreadyFullStudyRoom();
        this.nowUserCnt++;
    }

    public void decreaseNowUserCnt() {
        if(this.nowUserCnt - 1 <= 0) throw new EmptyStudyRoom();
        this.nowUserCnt--;
    }
}
