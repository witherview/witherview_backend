package com.witherview.selfPractice;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundUser;
import com.witherview.support.MockMvcSupporter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@Transactional
public class SelfPracticeSupporter extends MockMvcSupporter {
    final Long userId = (long)1;
    final String email = "hohoho@witherview.com";
    final String password = "123456";
    final String name = "witherview";

    final Long failedListId = (long) 3;
    // questionlist
    Long listId = (long) 1;
    final String title = "개발자 스터디 모집해요.";
    final String enterprise = "kakao";
    final String job = "sw 개발자";
    final String updatedTitle = "기획자 스터디 모집합니다.";
    final String updatedEnterprise = "naver";
    // question
    Long questionId = (long) 1;
    final Long failedQuestionId = (long) 3;
    final String question = "당신의 주 언어는 무엇인가요?";
    final String answer = "저의 주 언어는 ~~입니다";
    final Integer order = 1;
    final String updatedQuestion = "당신의 입사 후 목표는 무엇인가요?";
    final String updatedAnswer = "저의 목표는 ~입니다.";

    final MockHttpSession mockHttpSession = new MockHttpSession();

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionListRepository questionListRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void 회원가입_세션생성_리스트생성() {
        User user = userRepository.findByEmail(email);
        // 회원가입
        if (user == null) {
            user = userRepository.save(new User(email, passwordEncoder.encode(password), name));
        }
        // 세션생성
        if(mockHttpSession.getAttribute("user") == null) {
            AccountSession accountSession = new AccountSession(userId, email, name);
            mockHttpSession.setAttribute("user", accountSession);
        }
        // 리스트생성
        QuestionList questionList = new QuestionList("title", "enterprise", "job");
        if(questionListRepository.count() == 0) {
            user.addQuestionList(questionList);
            listId = questionListRepository.save(questionList).getId();
        }
    }
}
