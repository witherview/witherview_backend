package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class HostNotLeaveStudyRoom extends BusinessException {
  public HostNotLeaveStudyRoom() {
    super(ErrorCode.HOST_NOT_LEAVE_STUDYROOM);
  }
}
