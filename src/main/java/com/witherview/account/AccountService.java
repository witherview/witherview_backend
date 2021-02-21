package com.witherview.account;

import com.witherview.account.exception.DuplicateEmailException;
import com.witherview.account.exception.InvalidLoginException;
import com.witherview.account.exception.NotEqualPasswordException;
import com.witherview.account.exception.NotSavedProfileImgException;
import com.witherview.database.entity.*;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.img-location}")
    private String uploadLocation;

    @Value("${server.url}")
    private String serverUrl;



    @Transactional
    public User register(AccountDTO.RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new NotEqualPasswordException();
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) throw new DuplicateEmailException();

        User user = User.builder()
                .email(dto.getEmail())
                .encryptedPassword(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .mainIndustry(dto.getMainIndustry())
                .subIndustry(dto.getSubIndustry())
                .mainJob(dto.getMainJob())
                .subJob(dto.getSubJob())
                .build();

        String title = "기본 질문 리스트";
        String enterprise = "공통";
        String job = "공통";

        // todo: 질문 리스트를 쉽게 수정하거나 변경할 수 있는 로직이 있다면 좋을 것 같다.
        QuestionList questionList = new QuestionList(title, enterprise, job);
        List<String> list = new ArrayList<>();
        list.add("간단한 자기소개 해주세요."); list.add("이 직무를 선택하게 된 이유가 무엇인가요?");
        list.add("지원 직무의 핵심 역량은 무엇이라고 생각하나요?");  list.add("그 역량을 갖추기 위해 어떤 노력을 했는지 구체적으로 말씀해 주시겠어요?");
        list.add("비슷한 경험을 한 지원자들보다 내가 더 탁월하다고 이야기할 수 있는 근거는 무엇인가요?");
        list.add("본인 성격의 장점과 단점은 무엇이라고 생각하나요?"); list.add("팀 프로젝트에서 갈등 상황이 발생한다면 어떻게 해결하시는 편인가요?");
        list.add("우리 회사의 지원동기에 대해서 설명해주세요.");    list.add("우리 회사에 대해서 아는 대로 설명해보세요.");
        list.add("우리 회사 경쟁사는 어디라고 생각하나요? 그 경쟁사보다 우리가 더 나은 점은 무엇인가요?");
        list.add("우리 회사가 속한 산업의 전망이 어떻게 될 것 같나요?"); list.add("입사 후 우리 회사에서 이루고자 하는 목표가 있나요?");
        list.add("마지막으로 하고 싶은 이야기나 질문 있으신가요?");

        for (int i = 0; i < list.size(); i++) {
            questionList.addQuestion(new Question(list.get(i), "", i + 1));
        }
        user.addQuestionList(questionList);
        return userRepository.save(user);
    }

    @Transactional
    public void updateMyInfo(Long userId, AccountDTO.UpdateMyInfoDTO dto) {
        User user = findUser(userId);
        user.update(dto.getName(), dto.getMainIndustry(), dto.getSubIndustry(),
                dto.getMainJob(), dto.getSubJob());
    }

    @Transactional
    public User uploadProfile(Long userId, MultipartFile profileImg) {
        User user = findUser(userId);
        String fileOriName = profileImg.getOriginalFilename();
        String orgFileExtension = fileOriName.substring(fileOriName.lastIndexOf("."));
        String profileName = userId + "_" + UUID.randomUUID() + orgFileExtension;

        File newImg = new File(uploadLocation, profileName);
        try {
            profileImg.transferTo(newImg);
            user.uploadImg(serverUrl + "profiles/" + profileName);
        } catch(Exception e) {
            throw new NotSavedProfileImgException();
        }
        return user;
    }

    public User login(AccountDTO.LoginDTO dto) {
        var user = userRepository.findByEmail(dto.getEmail());
        if (!user.isPresent() || !passwordEncoder.matches(dto.getPassword(), user.get().getEncryptedPassword())) {
            throw new InvalidLoginException();
        }
        return user.get();
    }

    public AccountDTO.ResponseMyInfo myInfo(Long userId) {
        User user = findUser(userId);
        AccountDTO.ResponseMyInfo responseMyInfo = new AccountDTO.ResponseMyInfo();
        List<StudyFeedback> feedbackList = new ArrayList<>();

        for (StudyHistory v : user.getStudyHistories()) {
            feedbackList.addAll(v.getStudyFeedbacks());
        }

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
        Long questionListCnt = (long) user.getQuestionLists().size();

        responseMyInfo.setProfileImg(user.getProfileImg());
        responseMyInfo.setSelfPracticeCnt(user.getSelfPracticeCnt());
        responseMyInfo.setGroupStudyCnt(user.getGroupPracticeCnt());
        responseMyInfo.setPassCnt(passCnt);
        responseMyInfo.setFailCnt(failCnt);
        responseMyInfo.setInterviewScore(String.format("%.1f", interviewScore));
        responseMyInfo.setQuestionListCnt(questionListCnt);
        responseMyInfo.setMainIndustry(user.getMainIndustry());
        responseMyInfo.setSubIndustry(user.getSubIndustry());
        responseMyInfo.setMainJob(user.getMainJob());
        responseMyInfo.setSubJob(user.getSubJob());
        return responseMyInfo;
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 로그인하기 위해 접근했는데 아이디가 없는 경우
        var user = userRepository.findByEmail(email)
                .orElseThrow(InvalidLoginException::new);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getEncryptedPassword(), new ArrayList<>()
        );
    }
}
