package entity;

import java.time.LocalDateTime;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
// 방 id순 오름차순 정렬 해당 방에서는 시간순서로 정리.
@CompoundIndex(def = "{'studyRoomId' : 1, 'timestamp' : 1}")
public class Chat {

    @Id
    private String id;
    @NotNull
    private String userId;

    @NotNull
    private String userName;

    @NotNull
    private Long studyRoomId;

    @NotNull(message = "메시지는 반드시 입력해야 합니다.")
    private String message;

    private LocalDateTime timestamp;
}
