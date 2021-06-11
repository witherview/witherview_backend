package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class AlreadyJoinedStudyRoom extends BusinessException {
    public AlreadyJoinedStudyRoom() {
        super(ErrorCode.ALREADY_JOIN_STUDYROOM);
    }
}
