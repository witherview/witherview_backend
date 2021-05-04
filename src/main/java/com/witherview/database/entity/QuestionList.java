package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_question_list")
public class QuestionList {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User owner;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String enterprise;

    @NotBlank
    @Column(nullable = false)
    private String job;

    @OneToMany(mappedBy = "belongList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "questionList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfHistory> selfHistories = new ArrayList<>();

    @Builder
    public QuestionList(User owner, String title, String enterprise, String job) {
        this.title = title;
        this.enterprise = enterprise;
        this.job = job;
        this.owner = owner;
    }

    public void addQuestion(Question question) {
        question.updateBelongList(this);
        this.questions.add(question);
    }

    public void addSelfHistory(SelfHistory selfHistory) {
        this.selfHistories.add(selfHistory);
    }

    protected void updateOwner(User owner) {
        this.owner = owner;
    }

    public void update(String title, String enterprise, String job) {
        this.title = title;
        this.enterprise = enterprise;
        this.job = job;
    }
}
