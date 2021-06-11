package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotFoundQuestion extends BusinessException {
    public NotFoundQuestion() {
        super(ErrorCode.NOT_FOUND_QUESTION);
    }
}
