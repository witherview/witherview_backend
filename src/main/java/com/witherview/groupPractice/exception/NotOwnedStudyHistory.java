package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotOwnedStudyHistory extends BusinessException {
    public NotOwnedStudyHistory() {
        super(ErrorCode.NOT_OWNED_STUDYHISTORY);
    }
}
