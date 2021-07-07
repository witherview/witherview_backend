package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class EmptyStudyRoom extends BusinessException {
    public EmptyStudyRoom() {
        super(ErrorCode.EMPTY_STUDYROOM);
    }
}
