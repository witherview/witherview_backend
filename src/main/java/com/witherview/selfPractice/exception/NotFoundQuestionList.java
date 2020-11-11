package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundQuestionList extends BusinessException {
    public NotFoundQuestionList() {
        super(ErrorCode.NOT_FOUND_QUESTIONLIST);
    }
}