package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotEqualPassword extends BusinessException {
    public NotEqualPassword() {
        super(ErrorCode.NOT_EQUAL_PASSWORD);
    }
}
