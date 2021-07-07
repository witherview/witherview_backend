package exception.account;

import exception.BusinessException;
import exception.ErrorCode;

public class InvalidLoginException extends BusinessException {
    public InvalidLoginException() {
        super(ErrorCode.INVALID_LOGIN);
    }
}
