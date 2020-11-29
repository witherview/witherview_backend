package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundStudyRoom extends BusinessException {
    public NotFoundStudyRoom() {
        super(ErrorCode.NOT_FOUND_STUDYROOM);
    }
}
