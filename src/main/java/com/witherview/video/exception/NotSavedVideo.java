package com.witherview.video.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotSavedVideo extends BusinessException {
    public NotSavedVideo() {
        super(ErrorCode.NOT_SAVED_VIDEO);
    }
}
