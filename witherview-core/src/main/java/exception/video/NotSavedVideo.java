package exception.video;

import exception.BusinessException;
import exception.ErrorCode;

public class NotSavedVideo extends BusinessException {
    public NotSavedVideo() {
        super(ErrorCode.NOT_SAVED_VIDEO);
    }
}
