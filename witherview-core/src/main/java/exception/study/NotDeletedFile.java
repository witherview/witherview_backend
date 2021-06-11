package exception.study;

import exception.BusinessException;
import exception.ErrorCode;

public class NotDeletedFile extends BusinessException {
    public NotDeletedFile() {
        super(ErrorCode.NOT_DELETED_FILE);
    }
}
