package com.witherview.selfPractice.history;

import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.SelfHistory;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.SelfHistoryRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.exception.BusinessException;
import com.witherview.exception.ErrorCode;
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
    private final SelfHistoryRepository selfHistoryRepository;

    @Transactional
    public SelfHistory save(SelfHistoryDTO.SelfHistorySaveDTO dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        QuestionList questionList = questionListRepository.findById(dto.getQuestionListId())
                                    .orElseThrow(NotFoundQuestionList::new);
        if (!user.getId().equals(questionList.getOwner().getId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_QUESTIONLIST);
        }

        SelfHistory selfHistory = new SelfHistory(questionList);
        user.addSelfHistory(selfHistory);
        return selfHistoryRepository.save(selfHistory);
    }

    public List<SelfHistory> findAll(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        return selfHistoryRepository.findAllByUserId(user.getId());
    }
}
