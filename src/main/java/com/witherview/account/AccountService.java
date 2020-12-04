package com.witherview.account;

import com.witherview.account.exception.DuplicateEmail;
import com.witherview.account.exception.InvalidLogin;
import com.witherview.account.exception.NotEqualPassword;
import com.witherview.database.entity.*;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(AccountDTO.RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new NotEqualPassword();
        }
        User findUser = userRepository.findByEmail(dto.getEmail());
        if (findUser != null) {
            throw new DuplicateEmail();
        }
        User user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getName());
        String title = "기본 질문 리스트";
        String enterprise = "공통";
        String job = "공통";
        QuestionList questionList = new QuestionList(title, enterprise, job);
        questionList.addQuestion(new Question("간단한 자기소개 해주세요.", "", 1));
        questionList.addQuestion(new Question("왜 지원 직무가 하고 싶은 가요?", "", 2));
        questionList.addQuestion(new Question("지원 직무 핵심 역량은 무엇이라고 생각하나요?", "", 3));
        questionList.addQuestion(new Question("그 역량을 갖추기 위해 어떤 노력을 했는지 구체적으로 말씀해 주시겠어요?", "", 4));
        questionList.addQuestion(new Question("비슷한 경험을 한 지원자들보다 내가 더 탁월하다고 이야기할 수 있는 근거가 있을까요?", "", 5));
        questionList.addQuestion(new Question("직원 직무 관련 준비가 되었다고 주장할 게 있다면 1가지만 더 이야기 해보시겠어요?", "", 6));
        questionList.addQuestion(new Question("성격의 장점과 단점은 무엇인가요?", "", 7));
        questionList.addQuestion(new Question("팀 프로젝트에서 갈등 상황을 어떻게 해결하시는 편인가요?", "", 8));
        questionList.addQuestion(new Question("우리 회사 지원동기에 대해서 설명해주세요.", "", 9));
        questionList.addQuestion(new Question("우리 회사에 대해서 그냥 아는 대로 설명해보실래요?", "", 10));
        questionList.addQuestion(new Question("우리 회사 경쟁사는 어디라고 생각하나요? 그 경쟁사보다 우리가 더 나은 점은 무엇인가요?", "", 11));
        questionList.addQuestion(new Question("우리 회사가 속한 산업의 전망이 어떻게 될 것 같나요?", "", 12));
        questionList.addQuestion(new Question("입사 후 우리 회사에서 이루고자 하는 목표가 있나요?", "", 13));
        questionList.addQuestion(new Question("마지막으로 하고 싶은 이야기나 질문 있으신가요?", "", 14));
        user.addQuestionList(questionList);
        return userRepository.save(user);
    }

    public User login(AccountDTO.LoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidLogin();
        }
        return user;
    }

    public AccountDTO.ResponseMyInfo myInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        AccountDTO.ResponseMyInfo responseMyInfo = new AccountDTO.ResponseMyInfo();
        List<StudyFeedback> feedbackList = user.getStudyFeedbacks();

        Double interviewScore = feedbackList
                .stream()
                .mapToDouble(StudyFeedback::getScore)
                .average()
                .orElse(0);
        Long passCnt = feedbackList
                .stream()
                .filter(StudyFeedback::getPassOrFail)
                .count();
        Long failCnt = feedbackList.size() - passCnt;
        Long studyCnt = (long) feedbackList
                .stream()
                .map(StudyFeedback::getStudyRoom)
                .map(StudyRoom::getId)
                .collect(Collectors.toSet())
                .size();
        Long questionListCnt = (long) user.getQuestionLists().size();

        responseMyInfo.setSelfPracticeCnt(user.getSelfPracticeCnt());
        responseMyInfo.setGroupStudyCnt(studyCnt);
        responseMyInfo.setPassCnt(passCnt);
        responseMyInfo.setFailCnt(failCnt);
        responseMyInfo.setInterviewScore(String.format("%.1f", interviewScore));
        responseMyInfo.setQuestionListCnt(questionListCnt);
        return responseMyInfo;
    }
}
