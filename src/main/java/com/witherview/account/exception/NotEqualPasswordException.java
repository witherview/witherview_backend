package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotEqualPasswordException extends BusinessException {
    public NotEqualPasswordException() {
        super(ErrorCode.NOT_EQUAL_PASSWORD);
    }
}
