package com.witherview.selfPractice.history;

import com.witherview.database.entity.SelfHistory;
import com.witherview.database.entity.User;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.database.repository.SelfCheckRepository;
import com.witherview.database.repository.SelfHistoryRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.selfPractice.exception.NotDeletedFile;
import com.witherview.selfPractice.exception.NotFoundHistory;
import com.witherview.selfPractice.exception.NotFoundQuestionList;
import com.witherview.selfPractice.exception.NotOwnedSelfHistory;
import com.witherview.selfPractice.exception.UserNotFoundException;
import com.witherview.video.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SelfHistoryService {
    private final UserRepository userRepository;
    private final QuestionListRepository questionListRepository;
    private final SelfHistoryRepository selfHistoryRepository;
    private final SelfCheckRepository selfCheckRepository;
    private final VideoService videoService;

    @Value("${upload.location}")
    private String uploadLocation;

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
        String savedLocation = videoService.upload(videoFile,
                                           userId + "/self/" + selfHistory.getId());
        selfHistory.updateSavedLocation(savedLocation);
        return selfHistory;
    }

    @Transactional
    public SelfHistory deleteHistory(String userId, Long historyId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        SelfHistory selfHistory = selfHistoryRepository.findById(historyId).orElseThrow(NotFoundHistory::new);
        authenticateOwner(user, selfHistory);

        selfCheckRepository.deleteAll(selfHistory.getSelfCheckList());
        selfHistoryRepository.delete(selfHistory);

        String uploadedPathWithId = uploadLocation + user.getEmail() + "/self/" + selfHistory.getId();
        ArrayList<File> willDeleteFiles = new ArrayList<>();
        willDeleteFiles.add(new File(uploadedPathWithId + ".m3u8"));
        willDeleteFiles.add(new File(uploadedPathWithId + ".webm"));
        for (File f: willDeleteFiles) {
            if (f.exists() && !f.delete()) {
                throw new NotDeletedFile();
            }
        }

        int i = 0;
        while (true) {
            File f = new File(uploadedPathWithId + i + ".ts");
            if (!f.exists()) break;
            if (!f.delete()) throw new NotDeletedFile();
            i++;
        }

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
