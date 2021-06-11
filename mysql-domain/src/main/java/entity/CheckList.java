package entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public CheckList(String checkList) {
        this.checkList = checkList;
    }

    protected void updateCheckListType(CheckListType checkListType) {
        this.checkListType = checkListType;
    }
}
