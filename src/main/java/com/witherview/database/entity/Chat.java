package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_chat")
public class Chat extends CreatedBaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id", nullable = false)
    private StudyRoom studyRoom;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Builder
    public Chat(User user, String message) {
        this.user = user;
        this.message = message;
    }

    protected void updateStudyRoom(StudyRoom studyRoom) {
        this.studyRoom = studyRoom;
    }
}
