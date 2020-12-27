package com.witherview.groupPractice;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.StudyFeedback;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.StudyRoomParticipant;
import com.witherview.database.entity.User;
import com.witherview.database.repository.StudyFeedbackRepository;
import com.witherview.database.repository.StudyRoomRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.support.MockMvcSupporter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

@Transactional
public class GroupStudySupporter extends MockMvcSupporter {
    final String email1 = "hohoho@witherview.com";
    final String password1 = "123456";
    final String name1 = "witherview";
    final String email2 = "yayaya@witherview.com";
    final String password2 = "123456";
    final String name2 = "yapp";
    final String email3 = "yoyoyo@witherview.com";
    final String password3 = "123456";
    final String name3 = "web2";
    final String title1 = "네이버 빅데이터 플랫폼 스터디 모집합니다.";
    final String description1 =  "코딩테스트 합격자만 신청바랍니다!!!!";
    final String industry1 = "it서비스";
    final String job1 = "개발자";
    final LocalDate date1 = LocalDate.parse("2020-12-03");
    final LocalTime time1 = LocalTime.parse("15:00:01");
    final String title2 = "카카오 백엔드 스터디 모집합니다.";
    final String description2 =  "같이 면접 준비 하실 분 구합니다~~~";
    final LocalDate date2 = LocalDate.parse("2020-12-04");
    final LocalTime time2 = LocalTime.parse("12:00:01");
    final String updatedTitle = "카카오 백엔드 스터디 모집합니다.";
    final String updatedDescription = "함께 스터디 하실 분 들어요세요";
    final Byte score = 80;
    final Boolean passOrFail = true;
    final MockHttpSession mockHttpSession = new MockHttpSession();
    Long userId1 = (long)1;
    Long userId2 = (long)2;
    Long userId3 = (long)3;
    Long roomId = (long) 4;
    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRoomRepository studyRoomRepository;

    @Autowired
    StudyFeedbackRepository studyFeedbackRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void 회원가입_세션생성_스터디룸생성() {
        // 회원가입
        User user1 = userRepository.save(new User(email1, passwordEncoder.encode(password1), name1,
                "주 관심산업", "부 관심산업", "주 관심직무", "부 관심직무"));
        userId1 = user1.getId();

        User user2 = userRepository.save(new User(email2, passwordEncoder.encode(password2), name2,
                "주 관심산업", "부 관심산업", "주 관심직무", "부 관심직무"));
        userId2 = user2.getId();

        User user3 = userRepository.save(new User(email3, passwordEncoder.encode(password3), name3,
                "주 관심산업", "부 관심산업", "주 관심직무", "부 관심직무"));
        userId3 = user3.getId();

        // 세션생성
        AccountSession accountSession = new AccountSession(userId1, email1, name1);
        mockHttpSession.setAttribute("user", accountSession);

        // 스터디룸생성
        StudyRoom studyRoom = new StudyRoom(title1, description1, industry1, job1, date1, time1);
        user1.addHostedRoom(studyRoom);
        roomId = studyRoomRepository.save(studyRoom).getId();

        StudyRoomParticipant studyRoomParticipant1 = StudyRoomParticipant.builder()
                .studyRoom(studyRoom)
                .user(user1)
                .build();

        studyRoom.addParticipants(studyRoomParticipant1);
        user1.addParticipatedRoom(studyRoomParticipant1);

        StudyRoomParticipant studyRoomParticipant2 = StudyRoomParticipant.builder()
                .studyRoom(studyRoom)
                .user(user3)
                .build();

        studyRoom.addParticipants(studyRoomParticipant2);
        user3.addParticipatedRoom(studyRoomParticipant2);

        // 피드백 등록
        StudyFeedback studyFeedback1 = StudyFeedback.builder()
                .studyRoom(roomId)
                .writtenUser(user1)
                .score(score)
                .passOrFail(passOrFail)
                .build();

        user3.addStudyFeedback(studyFeedback1);
        studyFeedbackRepository.save(studyFeedback1);

        StudyFeedback studyFeedback2 = StudyFeedback.builder()
                .studyRoom(roomId)
                .writtenUser(user3)
                .score(score)
                .passOrFail(passOrFail)
                .build();

        user1.addStudyFeedback(studyFeedback2);
        studyFeedbackRepository.save(studyFeedback2);
    }
}
