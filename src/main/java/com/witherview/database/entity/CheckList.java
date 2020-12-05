package com.witherview.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_checklist")
public class CheckList {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_list_type_id", nullable = false)
    private CheckListType checkListType;

    @NotNull @NotBlank
    private String checkList;

    protected void updateCheckListType(CheckListType checkListType) {
        this.checkListType = checkListType;
    }
}
