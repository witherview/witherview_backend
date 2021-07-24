package com.witherview.study.service;

import com.witherview.mysql.entity.SelfHistory;
import com.witherview.mysql.entity.User;
import com.witherview.mysql.repository.QuestionListRepository;
import com.witherview.mysql.repository.SelfCheckRepository;
import com.witherview.mysql.repository.SelfHistoryRepository;
import com.witherview.mysql.repository.UserRepository;
import com.witherview.upload.service.DeleteService;
import com.witherview.upload.service.UploadService;
import exception.study.NotFoundHistory;
import exception.study.NotFoundQuestionList;
import exception.study.NotOwnedSelfHistory;
import exception.study.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfHistoryService {
    private final UserRepository userRepository;
    private final QuestionListRepository questionListRepository;
    private final SelfHistoryRepository selfHistoryRepository;
    private final SelfCheckRepository selfCheckRepository;
    private final UploadService uploadService;
    private final DeleteService deleteService;

    @Transactional
    public SelfHistory save(Long questionListId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        questionListRepository.findById(questionListId).orElseThrow(NotFoundQuestionList::new);

        SelfHistory selfHistory = new SelfHistory(questionListId);
        user.addSelfHistory(selfHistory);
        selfHistoryRepository.save(selfHistory);
        user.increaseSelfPracticeCnt();
        return selfHistory;
    }

    @Transactional
    public SelfHistory uploadVideo(MultipartFile videoFile, Long historyId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        SelfHistory selfHistory = selfHistoryRepository.findById(historyId).orElseThrow(NotFoundHistory::new);
        authenticateOwner(user, selfHistory);

        var loc = uploadService.upload(userId, videoFile);
        selfHistory.updateSavedLocation(loc);
        return selfHistory;
    }

    @Transactional
    public SelfHistory deleteHistory(String userId, Long historyId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        SelfHistory selfHistory = selfHistoryRepository.findById(historyId).orElseThrow(NotFoundHistory::new);
        authenticateOwner(user, selfHistory);

        selfCheckRepository.deleteAll(selfHistory.getSelfCheckList());
        selfHistoryRepository.delete(selfHistory);

        deleteService.delete(selfHistory.getSavedLocation());
        return selfHistory;
    }

    public List<SelfHistory> findAll(String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return selfHistoryRepository.findAllByUserId(user.getId());
    }

    private void authenticateOwner(User user, SelfHistory selfHistory) {
        if (!user.getId().equals(selfHistory.getUser().getId())) {
            throw new NotOwnedSelfHistory();
        }
    }
}
