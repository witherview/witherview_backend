package com.witherview.database.entity;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
@NoArgsConstructor
@Table(name = "tbl_self_study")
public class SelfHistory extends CreatedBaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private Long questionListId;

    private String historyTitle;

    private String savedLocation;

    @OneToMany(mappedBy = "selfHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfCheck> selfCheckList = new ArrayList<>();

    @Builder
    public SelfHistory(Long questionListId) {
        this.questionListId = questionListId;
    }

    public void updateHistoryTitle(String historyTitle) { this.historyTitle = historyTitle; }

    public void updateSavedLocation(String savedLocation) {
        this.savedLocation = savedLocation;
    }

    public void addSelfCheck(SelfCheck selfCheck) {
        selfCheck.updateSelfHistory(this);
        this.selfCheckList.add(selfCheck);
    }
}
