package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotSavedProfileImg extends BusinessException {
    public NotSavedProfileImg() {
        super(ErrorCode.NOT_SAVED_PROFILE);
    }
}
