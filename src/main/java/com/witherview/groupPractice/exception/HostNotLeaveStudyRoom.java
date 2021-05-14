package com.witherview.groupPractice.exception;

import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;

public class HostNotLeaveStudyRoom extends BusinessException {
  public HostNotLeaveStudyRoom() {
    super(ErrorCode.HOST_NOT_LEAVE_STUDYROOM);
  }
}
