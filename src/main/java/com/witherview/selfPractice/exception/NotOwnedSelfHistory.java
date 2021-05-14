package com.witherview.selfPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class NotOwnedSelfHistory extends BusinessException {
  public NotOwnedSelfHistory() {
    super(ErrorCode.NOT_OWNED_SELFHISTORY);
  }
}
