package com.witherview.account;

import com.witherview.database.entity.User;
import com.witherview.database.repository.UserRepository;
import com.witherview.support.MockMvcSupporter;
import com.witherview.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@Transactional
@ActiveProfiles("test")
public abstract class AccountSupporter extends MockMvcSupporter {
    final String mainIndustry1 = "IT서비스";
    final String email = "hohoho@witherview.com";
    final String email1 = "hahaha@witherview.com";
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
    final String profileImg = "http://localhost:8080/profiles/1_c27990ec-c038-4ec6-9ad2-9c279cde3a3b.png";
    final Long groupStudyCnt = 0L;
    final Long selfPracticeCnt = 0L;
    final Long questionListCnt = 0L;
    final String interviewScore = "0.00";
    final Long passCnt = 0L;
    final Long failCnt = 0L;
    String userId;
    String token;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void 회원가입_토큰생성_스터디룸생성() {
        // 회원가입
        User user = userRepository.save(new User(email1, passwordEncoder.encode(password), name,
                mainIndustry1, subIndustry1, mainJob1, subJob1, "01000000000"));

        userId = user.getId();
        user.uploadImg(profileImg);
        token = new JwtUtils().createToken(user.getEmail(), user.getId());

    }
}
