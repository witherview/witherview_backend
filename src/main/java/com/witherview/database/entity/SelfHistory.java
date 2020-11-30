package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_self_study")
public class SelfHistory extends CreatedBaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_list_id", nullable = false)
    private QuestionList questionList;

    @NotNull @NotBlank
    private String savedLocation;

    @Builder
    public SelfHistory(QuestionList questionList) {
        this.questionList = questionList;
    }

    protected void updateUser(User user) {
        this.user = user;
    }

    public void updateSavedLocation(String savedLocation) {
        this.savedLocation = savedLocation;
    }
}
