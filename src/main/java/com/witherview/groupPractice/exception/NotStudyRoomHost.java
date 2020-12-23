package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotStudyRoomHost extends BusinessException {
    public NotStudyRoomHost() {
        super(ErrorCode.NOT_STUDYROOM_HOST);
    }
}
