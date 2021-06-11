package entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
