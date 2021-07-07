package com.witherview;

import com.witherview.database.entity.Question;
import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.utils.GenerateRandomId;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestionListRepository questionListRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.findAll().isEmpty()) {
            User user = User.builder()
                    .email("test7@test.com")
                    .encryptedPassword(passwordEncoder.encode("123456"))
                    .name("test7")
                    .mainIndustry("mainIndustry")
                    .subIndustry("subIndustry")
                    .mainJob("mainJob")
                    .subJob("subJob")
                    .phoneNumber("01000000000")
                    .build();
            user.setId(new GenerateRandomId().generateId());
            var savedUser = userRepository.save(user);

            String title = "기본 질문 리스트";
            String enterprise = "공통";
            String job = "공통";

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
        }
    }
}
