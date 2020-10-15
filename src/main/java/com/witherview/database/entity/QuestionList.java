package com.witherview.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull
    @Column(nullable = false)
    private Integer order;
}
