package exception.account;

import exception.BusinessException;
import exception.ErrorCode;

public class NotSavedProfileImgException extends BusinessException {
    public NotSavedProfileImgException() {
        super(ErrorCode.NOT_SAVED_PROFILE);
    }
}
