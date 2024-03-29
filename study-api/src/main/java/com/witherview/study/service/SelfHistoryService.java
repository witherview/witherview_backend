package com.witherview.study.service;

import com.witherview.mysql.entity.SelfHistory;
import com.witherview.mysql.entity.User;
import com.witherview.mysql.repository.QuestionListRepository;
import com.witherview.mysql.repository.SelfCheckRepository;
import com.witherview.mysql.repository.SelfHistoryRepository;
import com.witherview.mysql.repository.UserRepository;
import com.witherview.study.dto.SelfHistoryDTO;
import com.witherview.upload.service.DeleteService;
import com.witherview.upload.service.UploadService;
import exception.study.NotFoundHistory;
import exception.study.NotFoundQuestionList;
import exception.study.NotOwnedSelfHistory;
import exception.study.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final int pageSize = 6;

    @Transactional
    public SelfHistory saveSelfHistory(String userId, Long questionListId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        questionListRepository.findById(questionListId).orElseThrow(NotFoundQuestionList::new);

            SelfHistory selfHistory = SelfHistory.builder()
                                                 .questionListId(questionListId)
                                                 .historyTitle("제목없음")
                                                 .build();
        user.addSelfHistory(selfHistory);
        user.increaseSelfPracticeCnt();
        return selfHistoryRepository.save(selfHistory);
    }

    @Transactional
    public SelfHistory uploadVideo(MultipartFile videoFile, Long historyId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        SelfHistory selfHistory = selfHistoryRepository.findById(historyId).orElseThrow(NotFoundHistory::new);
        authenticateOwner(user, selfHistory);

        var loc = uploadService.upload(userId, videoFile);
        selfHistory.updateSavedLocation(loc);
        selfHistory.updateThumbnail(loc.replace(".m3u8", ".png"));
        selfHistory.updateVideoInfo(loc.replace(".m3u8", ".json"));
        return selfHistory;
    }

    @Transactional
    public SelfHistory updateSelfHistory(String userId, SelfHistoryDTO.SelfHistoryUpdateRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        SelfHistory selfHistory = selfHistoryRepository.findById(dto.getHistoryId()).orElseThrow(NotFoundHistory::new);
        authenticateOwner(user, selfHistory);

        selfHistory.updateHistoryTitle(dto.getHistoryTitle());
        return selfHistory;
    }

    @Transactional
    public SelfHistory deleteSelfHistory(String userId, Long historyId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        SelfHistory selfHistory = selfHistoryRepository.findById(historyId).orElseThrow(NotFoundHistory::new);
        authenticateOwner(user, selfHistory);

        selfCheckRepository.deleteAll(selfHistory.getSelfCheckList());
        selfHistoryRepository.delete(selfHistory);

        //deleteService.delete(selfHistory.getSavedLocation());
        return selfHistory;
    }

    public List<SelfHistory> findAll(String userId, Integer lastPage) {
        int page = lastPage == null ? 0 : lastPage;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return selfHistoryRepository.findAllByUserId(user.getId(), pageable);
    }

    private void authenticateOwner(User user, SelfHistory selfHistory) {
        if (!user.getId().equals(selfHistory.getUser().getId())) {
            throw new NotOwnedSelfHistory();
        }
    }
}
