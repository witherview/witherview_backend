package com.witherview.mysql.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
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
