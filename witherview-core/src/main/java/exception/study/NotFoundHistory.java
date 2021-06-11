package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotFoundHistory extends BusinessException {
    public NotFoundHistory() {
        super(ErrorCode.NOT_FOUND_HISTORY);
    }
}
