package com.witherview.account;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.witherview.account.exception.DuplicateEmailException;
import com.witherview.account.exception.InvalidLoginException;
import com.witherview.account.exception.NotEqualPasswordException;
import com.witherview.account.exception.NotSavedProfileImgException;
import com.witherview.account.exception.StudyHostNotWithdrawUser;
import com.witherview.database.entity.*;
import com.witherview.database.repository.*;
import com.witherview.selfPractice.exception.UserNotFoundException;
import com.witherview.utils.AccountMapper;
import com.witherview.utils.GenerateRandomId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final UserRepository userRepository;
    private final StudyFeedbackRepository studyFeedbackRepository;
    private final QuestionListRepository questionListRepository;
    private final QuestionRepository questionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final RestTemplate restTemplate;
    private final AmazonS3 s3Client;

    @Value("${upload.img-location}")
    private String uploadLocation;
    @Value("${server.url}")
    private String serverUrl;
    @Value("${spring.security.oauth2.client.registration.witherview.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.witherview.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.witherview.scope}")
    private String scope;
    @Value("${spring.security.oauth2.client.registration.witherview.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.provider.witherview.token-uri}")
    private String tokenUri;
    @Value("${application.bucket.profile}")
    private String bucketName;

    private static String CDNUrl = "https://d1kb4tgqkzokon.cloudfront.net/";


    @Transactional
    public User register(AccountDTO.RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm()))
            throw new NotEqualPasswordException();
        if (userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new DuplicateEmailException();

        User user = User.builder()
                .email(dto.getEmail())
                .encryptedPassword(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .mainIndustry(dto.getMainIndustry())
                .subIndustry(dto.getSubIndustry())
                .mainJob(dto.getMainJob())
                .subJob(dto.getSubJob())
                .phoneNumber(dto.getPhoneNumber())
                .build();
        user.setId(new GenerateRandomId().generateId());
        var savedUser = userRepository.save(user);

        String title = "기본 질문 리스트";
        String enterprise = "공통";
        String job = "공통";

        // todo: 질문 리스트를 쉽게 수정하거나 변경할 수 있는 로직이 있다면 좋을 것 같다.
        QuestionList questionList = new QuestionList(savedUser.getId(), title, enterprise, job);
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
        questionListRepository.save(questionList);
        return savedUser;
    }
    @Transactional
    public User updateMyInfo(String userId, AccountDTO.UpdateMyInfoDTO dto) {
        User user = findUserById(userId);
        user.update(dto.getName(), dto.getMainIndustry(), dto.getSubIndustry(),
                dto.getMainJob(), dto.getSubJob(), dto.getPhoneNumber());
        return user;
    }

    @Transactional
    public User uploadProfileOnAWS(String userId, MultipartFile profileImg) {
        User user = findUserById(userId);
        String fileOriName = profileImg.getOriginalFilename();
        String orgFileExtension = fileOriName.substring(fileOriName.lastIndexOf("."));
        String profileName = user.getId() + "_" + UUID.randomUUID() + orgFileExtension;

        File newImg = new File(Path.of("").toAbsolutePath().toString(), fileOriName);
        try {
            profileImg.transferTo(newImg);
            // s3에 이미지 업로드
            // bucketName, key, value
            s3Client.putObject(
                    new PutObjectRequest(bucketName, profileName, newImg)
            );
            newImg.delete();
            // todo: s3 링크를 그냥 줄지, 클라우드프론트 등의 작업을 할지 / 내부 api로 대체할지 고민해야 함.
//            var url = s3Client.getUrl(bucketName, profileName).toString();
            var url = CDNUrl + profileName;
            user.uploadImg(url);
        } catch(Exception e) {
            throw new NotSavedProfileImgException();
        }
        return user;
    }

    public AccountDTO.ResponseMyInfo myInfo(String userId) {
        User user = findUserById(userId);

        var interviewScore =
                studyFeedbackRepository.getAvgInterviewScoreById(userId).orElse(0d);
        var passFailData = studyFeedbackRepository.getPassOrFailCountById(userId);
        Long passCnt = 0l;;
        if (passFailData.get(0)[1] != null) {
            passCnt = (Long) passFailData.get(0)[1];
        }
        Long failCnt = (Long) passFailData.get(0)[0] - passCnt;
        Long questionListCnt = questionRepository.CountByOwnerId(userId);

        var responseMyInfo = accountMapper.toResponseMyInfo(user);
        responseMyInfo.setInterviewScore(String.format("%.2f", interviewScore));
        responseMyInfo.setPassCnt(passCnt);
        responseMyInfo.setFailCnt(failCnt);
        responseMyInfo.setQuestionListCnt(questionListCnt);
        responseMyInfo.setProfileImg(user.getProfileImg());
        responseMyInfo.setSelfPracticeCnt(user.getSelfPracticeCnt());
        responseMyInfo.setGroupStudyCnt(user.getGroupPracticeCnt());
        return responseMyInfo;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
    public User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public List<StudyRoom> findRooms(String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getParticipatedStudyRooms()
                .stream()
                .map(r -> r.getStudyRoom())
                .collect(Collectors.toList());
    }

    public AccountDTO.ResponseTokenDTO login(String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", email);
        map.add("password", password);
        map.add("scope", scope);
        map.add("grant_type", grantType);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        var response = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, AccountDTO.ResponseTokenDTO.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new InvalidLoginException();
        }
        return response.getBody();
    }

    @Transactional
    public void withdrawUser(String userId) {
        User user = findUserById(userId);
        // 스터디룸 호스트인 경우 -> 회원탈퇴 실패
        user.getParticipatedStudyRooms().forEach(e -> {
            if(e.getStudyRoom().getHost().equals(userId)) throw new StudyHostNotWithdrawUser();
        });
        // 셀프 연습영상 삭제
        // 그룹 스터디 영상 삭제
        userRepository.delete(user);
    }

    public boolean isPasswordEquals(String userId, String password) {
        var dbPassword = findUserById(userId).getEncryptedPassword();
        var result = passwordEncoder.matches(password, dbPassword);
        return result;
    }

    public byte[] getFileFromS3(String userId) {
        var user = findUserById(userId);
        String fileName = user.getProfileImg();
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public String deleteFileFromS3(String userId) {
        User user = findUserById(userId);
        if (user.getProfileImg() == null) {
            return null;
        }
        String[] url = user.getProfileImg().split("/");
        String key = url[url.length-1];
        if (!key.isEmpty() && (key != null)) {
            s3Client.deleteObject(bucketName, key);
        }
        user.setProfileImg(null);
        return null;
    }
}
