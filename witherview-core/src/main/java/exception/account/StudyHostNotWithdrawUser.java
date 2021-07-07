package exception.account;

import exception.BusinessException;
import exception.ErrorCode;

public class StudyHostNotWithdrawUser extends BusinessException {
  public StudyHostNotWithdrawUser() {
    super(ErrorCode.STUDYHOST_NOT_WITHDRAW_USER);
  }
}
