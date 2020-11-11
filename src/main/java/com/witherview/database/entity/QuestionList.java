package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public QuestionList(String title, String enterprise, String job) {
        this.title = title;
        this.enterprise = enterprise;
        this.job = job;
    }

    public void addQuestion(Question question) {
        question.updateBelongList(this);
        this.questions.add(question);
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
