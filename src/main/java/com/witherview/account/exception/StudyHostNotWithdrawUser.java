package com.witherview.account.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class StudyHostNotWithdrawUser extends BusinessException {
  public StudyHostNotWithdrawUser() {
    super(ErrorCode.STUDYHOST_NOT_WITHDRAW_USER);
  }
}
