package exception.account;

import exception.BusinessException;
import exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
