package com.witherview.database.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @Builder @AllArgsConstructor
@Table(name = "tbl_checklist_type")
public class CheckListType {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    private String checkListField;

    @OneToMany(mappedBy = "checkListType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckList> checkLists = new ArrayList<>();

    public void addCheckList(CheckList checkList) {
        checkList.updateCheckListType(this);
        this.checkLists.add(checkList);
    }
}
