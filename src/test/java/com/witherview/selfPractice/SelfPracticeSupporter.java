package com.witherview.selfPractice;

import com.witherview.account.AccountSession;
import com.witherview.support.MockMvcSupporter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpSession;

public class SelfPracticeSupporter extends MockMvcSupporter {
    final Long userId = (long)1;
    final String email = "hohoho@witherview.com";
    final String name = "witherview";

    // questionlist
    final String title = "개발자 스터디 모집해요.";
    final String enterprise = "kakao";
    final String job = "sw 개발자";
    final Long listId = (long) 2;
    final String updatedTitle = "기획자 스터디 모집합니다.";
    final String updatedEnterprise = "naver";

    // question
    final Long questionId = (long) 1;
    final String question = "당신의 주 언어는 무엇인가요?";
    final String answer = "저의 주 언어는 ~~입니다";
    final Integer order = 1;
    final String updatedQuestion = "당신의 입사 후 목표는 무엇인가요?";
    final String updatedAnswer = "저의 목표는 ~입니다.";

    final MockHttpSession mockHttpSession = new MockHttpSession();

    @BeforeEach
    public void 세션생성() {
        AccountSession accountSession = new AccountSession(userId, email, name);
        mockHttpSession.setAttribute("user", accountSession);
    }
}
