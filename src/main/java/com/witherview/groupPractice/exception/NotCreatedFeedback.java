package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotCreatedFeedback extends BusinessException {
    public NotCreatedFeedback() {
        super(ErrorCode.NOT_CREATED_FEEDBACK);
    }
}
