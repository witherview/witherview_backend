package com.witherview.database.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "tbl_self_check")
public class SelfCheck {
    @Id
    @GeneratedValue
    private Long id;

    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_history_id", nullable = false)
    private SelfHistory selfHistory;

    @NotNull
    private Long checkListTypeId;

    @NotNull
    private Boolean isChecked;

    @NotNull
    private String checkListField;

    public void updateSelfHistory(SelfHistory selfHistory) {
        this.selfHistory = selfHistory;
    }
}
