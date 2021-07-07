package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotFoundCheckList extends BusinessException {
    public NotFoundCheckList() {
        super(ErrorCode.NOT_FOUND_CHECKLIST);
    }
}
