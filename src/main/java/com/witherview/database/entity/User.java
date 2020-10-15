package com.witherview.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@Table(name = "tbl_user")
public class User {

    @Id @GeneratedValue
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String refreshToken;

    private String mainIndustry;

    private String subIndustry;

    private String mainJob;

    private String subJob;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer studyCnt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer selfPracticeCnt;

    @Column(nullable = false)
    private Byte reliability;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionList> questionLists = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRoom> studyRooms = new ArrayList<>();
}
