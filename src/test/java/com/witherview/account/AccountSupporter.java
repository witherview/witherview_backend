package com.witherview.account;

import com.witherview.database.repository.UserRepository;
import com.witherview.support.MockMvcSupporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

public abstract class AccountSupporter extends MockMvcSupporter {
    final String mainIndustry1 = "IT서비스";
    final String email = "hohoho@witherview.com";
    final String password = "123456";
    final String passwordConfirm = "123456";
    final String name = "위더뷰";
    final String mainIndustry2 = "금융";
    final String subIndustry1 = "제조";
    final String subIndustry2 = "IT서비스";
    final String mainJob1 = "SW개발";
    final String mainJob2 = "기획자";
    final String subJob1 = "기획자";
    final String subJob2 = "SW개발";
    final Long groupStudyCnt = 0L;
    final Long selfPracticeCnt = 0L;
    final Long questionListCnt = 0L;
    final String interviewScore = "0.0";
    final Long passCnt = 0L;
    final Long failCnt = 0L;
    final MockHttpSession mockHttpSession = new MockHttpSession();
    Long userId = 0L;

    @Autowired
    UserRepository userRepository;
}
