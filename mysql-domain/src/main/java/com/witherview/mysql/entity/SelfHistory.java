package com.witherview.mysql.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_self_study")
public class SelfHistory extends CreatedBaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private Long questionListId;

    private String historyTitle;

    private String savedLocation;

    private String thumbnail;

    private String videoInfo;

    @OneToMany(mappedBy = "selfHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelfCheck> selfCheckList = new ArrayList<>();

    @Builder
    public SelfHistory(Long questionListId, String historyTitle) {

        this.questionListId = questionListId;
        this.historyTitle = historyTitle;
    }

    protected void updateUser(User user) {
        this.user = user;
    }

    public void updateHistoryTitle(String historyTitle) { this.historyTitle = historyTitle; }

    public void updateSavedLocation(String savedLocation) {
        this.savedLocation = savedLocation;
    }

    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateVideoInfo(String videoInfo) {
        this.videoInfo = videoInfo;
    }

    public void addSelfCheck(SelfCheck selfCheck) {
        selfCheck.updateSelfHistory(this);
        this.selfCheckList.add(selfCheck);
    }
}
