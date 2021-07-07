package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotJoinedStudyRoom extends BusinessException {
    public NotJoinedStudyRoom() {
        super(ErrorCode.NOT_JOIN_STUDYROOM);
    }
}
