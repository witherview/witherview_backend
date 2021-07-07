package exception.chat;

import exception.BusinessException;
import exception.ErrorCode;

public class NotSavedFeedBackChat extends BusinessException {
    public NotSavedFeedBackChat() {
        super(ErrorCode.NOT_SAVED_FEEDBACKCHAT);
    }
}
