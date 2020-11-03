package com.witherview.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "COMMON001", "Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "COMMON003", "Access is Denied"),
    NOT_FOUND_EXCEPTION(404, "COMMON004", "Page Not Found"),
    METHOD_NOT_ALLOWED(405, "COMMON005", " Method Not Allowed"),
    UNSUPPORTED_MEDIA_TYPE(415,"COMMON015", "Unsupported Media Type"),

    // AUTH
    DUPLICATE_EMAIL(400, "AUTH001", "중복된 이메일 입니다. 다른 이메일을 사용해 주세요."),
    NOT_EQUAL_PASSWORD(400, "AUTH002", "입력하신 두 비밀번호가 일치하지 않습니다.");
  
    // Self-Practice
    NOT_FOUND_USER(400, "SELF-PRACTICE001", "해당 유저가 없습니다."),
    NOT_FOUND_QUESTIONLIST(400, "SELF-PRACTICE002", "해당 질문리스트가 없습니다."),
    NOT_FOUND_QUESTION(400, "SELF-PRACTICE003", "해당 질문이 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
