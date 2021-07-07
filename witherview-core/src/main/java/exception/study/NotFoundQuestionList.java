package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotFoundQuestionList extends BusinessException {
    public NotFoundQuestionList() {
        super(ErrorCode.NOT_FOUND_QUESTIONLIST);
    }
}