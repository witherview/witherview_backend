package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class AlreadyJoinedStudyRoom extends BusinessException {
    public AlreadyJoinedStudyRoom() {
        super(ErrorCode.ALREADY_JOIN_STUDYROOM);
    }
}
