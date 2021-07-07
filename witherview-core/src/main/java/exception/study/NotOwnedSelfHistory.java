package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotOwnedSelfHistory extends BusinessException {
  public NotOwnedSelfHistory() {
    super(ErrorCode.NOT_OWNED_SELFHISTORY);
  }
}
