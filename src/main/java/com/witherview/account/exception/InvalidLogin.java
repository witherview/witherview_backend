package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class InvalidLogin extends BusinessException {
    public InvalidLogin() {
        super(ErrorCode.INVALID_LOGIN);
    }
}
