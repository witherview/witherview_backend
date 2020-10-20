package com.witherview.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private User host;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private Byte nowUserCnt;

    @NotNull
    @Column(nullable = false)
    private Byte maxUserCnt;

    @Column(columnDefinition = "DATE")
    private LocalDate date;

    @Column(columnDefinition = "TIME")
    private LocalTime time;

    @OneToMany(mappedBy = "study_room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRoomParticipant> studyRoomParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "study_room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    public StudyRoom(String title, Byte nowUserCnt, Byte maxUserCnt, LocalDate date, LocalTime time) {
        this.title = title;
        this.nowUserCnt = nowUserCnt;
        this.maxUserCnt = maxUserCnt;
        this.date = date;
        this.time = time;
    }

    protected void updateHost(User host) {
        this.host = host;
    }
}
