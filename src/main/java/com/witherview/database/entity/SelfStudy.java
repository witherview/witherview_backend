package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_self_study")
public class SelfStudy extends CreatedBaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_list_id", nullable = false)
    private QuestionList questionList;

    @Builder
    public SelfStudy(QuestionList questionList) {
        this.questionList = questionList;
    }

    protected void updateUser(User user) {
        this.user = user;
    }
}
