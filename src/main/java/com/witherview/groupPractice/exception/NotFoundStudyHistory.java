package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundStudyHistory extends BusinessException {
    public NotFoundStudyHistory() {
        super(ErrorCode.NOT_FOUND_STUDYHISTORY);
    }
}
