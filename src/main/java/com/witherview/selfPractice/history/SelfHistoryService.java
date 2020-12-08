package com.witherview.selfPractice.history;

import com.witherview.account.AccountSession;
import com.witherview.database.entity.QuestionList;
import com.witherview.database.entity.SelfHistory;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.SelfHistoryRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotFoundHistory;
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
    public SelfHistory save(Long questionListId, AccountSession accountSession) {
        User user = userRepository.findById(accountSession.getId()).orElseThrow(NotFoundUser::new);
        QuestionList questionList = questionListRepository.findById(questionListId)
                .orElseThrow(NotFoundQuestionList::new);
        if (!user.getId().equals(questionList.getOwner().getId())) {
            throw new NotFoundQuestionList();
        }
        SelfHistory selfHistory = new SelfHistory(questionList);
        user.addSelfHistory(selfHistory);
        questionList.addSelfHistory(selfHistory);
        selfHistoryRepository.save(selfHistory);
        user.increaseSelfPracticeCnt();
        return selfHistory;
    }

    @Transactional
    public SelfHistory uploadVideo(MultipartFile videoFile, Long historyId, AccountSession accountSession) {
        User user = userRepository.findById(accountSession.getId()).orElseThrow(NotFoundUser::new);
        SelfHistory selfHistory = selfHistoryRepository.findById(historyId).orElseThrow(NotFoundHistory::new);
        if (!user.getId().equals(selfHistory.getUser().getId())) {
            throw new NotFoundHistory();
        }
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
