package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class DuplicateEmail extends BusinessException {
    public DuplicateEmail() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
