package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundQuestion extends BusinessException {
    public NotFoundQuestion() {
        super(ErrorCode.NOT_FOUND_QUESTION);
    }
}
