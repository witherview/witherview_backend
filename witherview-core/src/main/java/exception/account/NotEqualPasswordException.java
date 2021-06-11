package exception.account;

import exception.BusinessException;
import exception.ErrorCode;

public class NotEqualPasswordException extends BusinessException {
    public NotEqualPasswordException() {
        super(ErrorCode.NOT_EQUAL_PASSWORD);
    }
}
