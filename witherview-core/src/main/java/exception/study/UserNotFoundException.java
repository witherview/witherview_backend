package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}

