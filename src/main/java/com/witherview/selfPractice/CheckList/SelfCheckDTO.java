package com.witherview.selfPractice.CheckList;

import com.witherview.database.entity.SelfCheck;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SelfCheckDTO {

    @Getter @Setter
    public static class SelfCheckRequestDTO {
        @NotNull(message = "혼자 연습 내역 아이디는 반드시 입력해야 합니다.")
        private Long selfHistoryId;

        @NotNull(message = "체크리스트 결과는 반드시 입력해야 합니다.")
        private List<CheckListDTO> checkLists;
    }

    @Getter @Setter @Builder
    public static class CheckListDTO {
        @NotNull(message = "체크리스트 아이디는 반드시 입력해야 합니다.")
        private Long checkListId;

        @NotNull(message = "체크 여부는 반드시 입력해야 합니다.")
        private Boolean isChecked;

        public SelfCheck toEntity() {
            return SelfCheck.builder()
                    .checkListId(checkListId)
                    .isChecked(isChecked)
                    .build();
        }
    }

    @Getter @Setter
    public static class CheckListResponseDTO {
        private Long checkListTypeId;
        private String checkListType;
        private List<CheckListInfoDTO> checkLists;
    }

    @Getter @Setter
    public static class CheckListInfoDTO {
        private Long id;
        private String checkList;
        private Boolean isChecked;
    }

    @Getter @Setter
    public static class CheckListResultDTO {
        private Long id;
        private Long checkListId;
        private Boolean isChecked;
    }
}
