package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundUser extends BusinessException {
    public NotFoundUser() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}

