package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class AlreadyFullStudyRoom extends BusinessException {
    public AlreadyFullStudyRoom() {
        super(ErrorCode.ALREADY_FULL_STUDYROOM);
    }
}
