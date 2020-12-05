package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundHistory extends BusinessException {
    public NotFoundHistory() {
        super(ErrorCode.NOT_FOUND_HISTORY);
    }
}
