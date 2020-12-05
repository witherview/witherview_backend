package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundCheckListType extends BusinessException {
    public NotFoundCheckListType() {
        super(ErrorCode.NOT_FOUND_CHECKLIST_TYPE);
    }
}
