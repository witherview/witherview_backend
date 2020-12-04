package com.witherview.account;

import com.witherview.account.exception.DuplicateEmail;
import com.witherview.account.exception.InvalidLogin;
import com.witherview.account.exception.NotEqualPassword;
import com.witherview.database.entity.StudyFeedback;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.User;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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
        return userRepository.save(new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()), dto.getName()));
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
        long passCnt = feedbackList
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
