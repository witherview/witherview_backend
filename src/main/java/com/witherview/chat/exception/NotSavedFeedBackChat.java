package com.witherview.chat.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotSavedFeedBackChat extends BusinessException {
    public NotSavedFeedBackChat() {
        super(ErrorCode.NOT_SAVED_FEEDBACKCHAT);
    }
}
