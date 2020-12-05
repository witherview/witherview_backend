package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class EmptyStudyRoom extends BusinessException {
    public EmptyStudyRoom() {
        super(ErrorCode.EMPTY_STUDYROOM);
    }
}
