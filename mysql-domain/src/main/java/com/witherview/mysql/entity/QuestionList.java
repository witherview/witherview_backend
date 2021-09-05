package com.witherview.mysql.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_question_list", indexes = {@Index(name = "user", columnList = "userId")})
public class QuestionList {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String userId;

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
    public QuestionList(String userId, String title, String enterprise, String job) {
        this.userId = userId;
        this.title = title;
        this.enterprise = enterprise;
        this.job = job;
    }

    public void addQuestion(Question question) {
        question.updateBelongList(this);
        this.questions.add(question);
    }

    public void updateUser(String userId) {
        this.userId = userId;
    }

    public void update(String title, String enterprise, String job) {
        this.title = title;
        this.enterprise = enterprise;
        this.job = job;
    }
}
