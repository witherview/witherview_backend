package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotJoinedStudyRoom extends BusinessException {
    public NotJoinedStudyRoom() {
        super(ErrorCode.NOT_JOIN_STUDYROOM);
    }
}
