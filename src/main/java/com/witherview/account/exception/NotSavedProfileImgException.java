package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotSavedProfileImgException extends BusinessException {
    public NotSavedProfileImgException() {
        super(ErrorCode.NOT_SAVED_PROFILE);
    }
}
