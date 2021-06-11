package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotOwnedStudyHistory extends BusinessException {
    public NotOwnedStudyHistory() {
        super(ErrorCode.NOT_OWNED_STUDYHISTORY);
    }
}
