package exception.account;

import exception.BusinessException;
import exception.ErrorCode;

public class InvalidJwtTokenException extends BusinessException {
    public InvalidJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
