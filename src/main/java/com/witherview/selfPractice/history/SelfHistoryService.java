package com.witherview.selfPractice.history;

import com.witherview.account.AccountSession;
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
import com.witherview.video.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfHistoryService {
    private final UserRepository userRepository;
    private final QuestionListRepository questionListRepository;
    private final SelfHistoryRepository selfHistoryRepository;
    private final VideoService videoService;

    @Transactional
    public SelfHistory save(MultipartFile videoFile, Long questionListId, AccountSession accountSession) {
        User user = userRepository.findById(accountSession.getId()).orElseThrow(NotFoundUser::new);
        QuestionList questionList = questionListRepository.findById(questionListId)
                                    .orElseThrow(NotFoundQuestionList::new);
        if (!user.getId().equals(questionList.getOwner().getId())) {
            throw new NotFoundQuestionList();
        }

        SelfHistory selfHistory = new SelfHistory(questionList);
        user.addSelfHistory(selfHistory);
        selfHistory.updateSavedLocation("temp");
        selfHistoryRepository.save(selfHistory);
        String savedLocation = videoService.upload(videoFile,
                                           accountSession.getEmail() + "/self/" + selfHistory.getId());
        selfHistory.updateSavedLocation(savedLocation);
        return selfHistory;
    }

    public List<SelfHistory> findAll(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        return selfHistoryRepository.findAllByUserId(user.getId());
    }
}
