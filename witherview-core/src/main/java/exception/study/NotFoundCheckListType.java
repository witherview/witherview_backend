package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotFoundCheckListType extends BusinessException {
    public NotFoundCheckListType() {
        super(ErrorCode.NOT_FOUND_CHECKLIST_TYPE);
    }
}
