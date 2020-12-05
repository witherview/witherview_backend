package com.witherview.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_self_check")
public class SelfCheck {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_history_id", nullable = false)
    private SelfHistory selfHistory;

    @NotNull
    private Long checkListId;

    @NotNull
    private Boolean isChecked;

    @Builder
    public SelfCheck(Long checkListId, Boolean isChecked) {
        this.checkListId = checkListId;
        this.isChecked = isChecked;
    }

    protected void updateSelfHistory(SelfHistory selfHistory) {
        this.selfHistory = selfHistory;
    }
}
