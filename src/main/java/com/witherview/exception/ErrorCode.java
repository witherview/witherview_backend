package com.witherview.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "COMMON001", "Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "COMMON003", "Access is Denied"),
    NOT_FOUND_EXCEPTION(404, "COMMON004", "Page Not Found"),
    METHOD_NOT_ALLOWED(405, "COMMON005", " Method Not Allowed"),
    UNSUPPORTED_MEDIA_TYPE(415,"COMMON015", "Unsupported Media Type");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
