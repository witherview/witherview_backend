package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotStudyRoomHost extends BusinessException {
    public NotStudyRoomHost() {
        super(ErrorCode.NOT_STUDYROOM_HOST);
    }
}
