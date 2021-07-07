package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotCreatedFeedback extends BusinessException {
    public NotCreatedFeedback() {
        super(ErrorCode.NOT_CREATED_FEEDBACK);
    }
}
