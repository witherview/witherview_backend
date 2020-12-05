package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotFoundCheckList extends BusinessException {
    public NotFoundCheckList() {
        super(ErrorCode.NOT_FOUND_CHECKLIST);
    }
}
