package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotFoundStudyHistory extends BusinessException {
    public NotFoundStudyHistory() {
        super(ErrorCode.NOT_FOUND_STUDYHISTORY);
    }
}
