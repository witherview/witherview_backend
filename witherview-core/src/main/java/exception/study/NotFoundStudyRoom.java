package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotFoundStudyRoom extends BusinessException {
    public NotFoundStudyRoom() {
        super(ErrorCode.NOT_FOUND_STUDYROOM);
    }
}
