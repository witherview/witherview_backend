package com.witherview.selfPractice.history;

import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.SelfStudy;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.SelfStudyRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundQuestionList;
import com.witherview.selfPractice.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfHistoryService {
    private final UserRepository userRepository;
    private final QuestionListRepository questionListRepository;
    private final SelfStudyRepository selfStudyRepository;

    @Transactional
    public SelfStudy save(SelfHistoryDTO.SelfHistorySaveDTO dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        QuestionList questionList = questionListRepository.findById(dto.getQuestionListId())
                                    .orElseThrow(NotFoundQuestionList::new);

        SelfStudy selfStudy = new SelfStudy(questionList);
        user.addSelfStudy(selfStudy);
        return selfStudyRepository.save(selfStudy);
    }

    public List<SelfStudy> findAll(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        return selfStudyRepository.findAllByUserId(user.getId());
    }
}
