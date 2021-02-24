package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class CustomJwtRuntimeException extends BusinessException {
    public CustomJwtRuntimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
