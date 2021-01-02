package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotDeletedFile extends BusinessException {
    public NotDeletedFile() {
        super(ErrorCode.NOT_DELETED_FILE);
    }
}
