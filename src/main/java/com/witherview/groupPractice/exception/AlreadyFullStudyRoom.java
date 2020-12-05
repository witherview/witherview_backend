package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class AlreadyFullStudyRoom extends BusinessException {
    public AlreadyFullStudyRoom() {
        super(ErrorCode.ALREADY_FULL_STUDYROOM);
    }
}
