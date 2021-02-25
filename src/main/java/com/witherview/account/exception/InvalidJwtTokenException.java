package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class InvalidJwtTokenException extends BusinessException {
    public InvalidJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
