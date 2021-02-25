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
    INVALID_TOKEN(401, "AUTH004", "로그인 후 이용해 주세요."),
    INVALID_JWT_TOKEN(401, "AUTH004", "유효하지 않은 로그인입니다."),
    DUPLICATE_EMAIL(400, "AUTH001", "중복된 이메일 입니다. 다른 이메일을 사용해 주세요."),
    NOT_EQUAL_PASSWORD(400, "AUTH002", "입력하신 두 비밀번호가 일치하지 않습니다."),
    INVALID_LOGIN(404, "AUTH003", "입력하신 이메일 혹은 비밀번호가 일치하지 않습니다."),
    NOT_SAVED_PROFILE(500, "AUTH004", "프로필 이미지를 저장하는데 실패하였습니다."),

    // Self-Practice
    NOT_FOUND_USER(404, "SELF-PRACTICE001", "해당 유저가 없습니다."),
    NOT_FOUND_QUESTIONLIST(404, "SELF-PRACTICE002", "해당 질문리스트가 없습니다."),
    NOT_FOUND_QUESTION(404, "SELF-PRACTICE003", "해당 질문이 없습니다."),

    // Group-Practice
    NOT_FOUND_STUDYROOM(404, "GROUP-PRACTICE001", "해당 스터디룸이 없습니다."),
    ALREADY_JOIN_STUDYROOM(400, "GROUP-PRACTICE002", "이미 참여하고 있는 스터디룸입니다."),
    NOT_JOIN_STUDYROOM(400, "GROUP-PRACTICE003", "참여하지 않은 스터디룸입니다."),
    ALREADY_FULL_STUDYROOM(400, "GROUP-PRACTICE004", "이미 스터디룸이 꽉 차 있습니다."),
    EMPTY_STUDYROOM(400, "GROUP-PRACTICE005", "스터디룸에 참여자가 없으면 안됩니다."),
    NOT_CREATED_FEEDBACK(404, "GROUP-PRACTICE006", "스터디룸에 참여하지 않은 사람에게 피드백을 줄 수 없습니다."),
    NOT_STUDYROOM_HOST(400, "GROUP-PRACTICE007", "해당 스터디룸의 호스트가 아닙니다."),

    // group-History
    NOT_FOUND_STUDYHISTORY(404, "GROUP-HISTORY001", "해당 스터디 연습 내역이 없습니다."),
    NOT_OWNED_STUDYHISTORY(400, "GROUP-HISTORY002", "해당 스터디 연습 내역의 타겟 유저가 아닙니다."),

    // Self-History
    NOT_FOUND_HISTORY(404, "SELF-HISTORY001", "해당 혼자 연습 내역이 없습니다."),
    NOT_DELETED_FILE(500, "SELF_HISTORY002", "혼자 연습 영상 삭제에 실패하였습니다"),

    // Self-Check
    NOT_FOUND_CHECKLIST(404, "SELF-CHECK001", "해당 체크리스트가 없습니다."),
    NOT_FOUND_CHECKLIST_TYPE(404, "SELF-CHECK002", "해당 체크리스트 타입이 없습니다."),

    // Video
    NOT_SAVED_VIDEO(500, "VIDEO001", "비디오를 저장하는데 실패하였습니다."),

    // chat
    NOT_SAVED_FEEDBACKCHAT(500, "CHAT001", "피드메세지 저장에 실패하였습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
