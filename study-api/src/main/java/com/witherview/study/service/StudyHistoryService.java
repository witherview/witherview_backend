package com.witherview.study.service;

import com.witherview.mysql.entity.StudyHistory;
import com.witherview.mysql.entity.User;
import com.witherview.mysql.repository.StudyHistoryRepository;
import com.witherview.mysql.repository.StudyRoomParticipantRepository;
import com.witherview.mysql.repository.StudyRoomRepository;
import com.witherview.mysql.repository.UserRepository;
import com.witherview.study.dto.StudyHistoryDTO;
import com.witherview.upload.service.UploadService;
import exception.study.NotFoundHistory;
import exception.study.NotFoundStudyHistory;
import exception.study.NotFoundStudyRoom;
import exception.study.NotJoinedStudyRoom;
import exception.study.NotOwnedStudyHistory;
import exception.study.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class StudyHistoryService {

    private final UserRepository userRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyRoomParticipantRepository studyRoomParticipantRepository;
    private final UploadService uploadService;

    @Transactional
    public StudyHistory saveStudyHistory(Long studyRoomId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        studyRoomRepository.findById(studyRoomId).orElseThrow(NotFoundStudyRoom::new);
        studyRoomParticipantRepository.findByStudyRoomIdAndUserId(studyRoomId, userId).orElseThrow(NotJoinedStudyRoom::new);

        StudyHistory studyHistory = StudyHistory.builder()
                                                .studyRoom(studyRoomId)
                                                .historyTitle("제목없음")
                                                .build();
        user.addStudyHistory(studyHistory);
        user.increaseGroupPracticeCnt();
        return studyHistoryRepository.save(studyHistory);
    }

    @Transactional
    public StudyHistory uploadVideo(MultipartFile videoFile, Long historyId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        StudyHistory studyHistory = findStudyHistory(historyId);
        authenticateOwner(user, studyHistory);

        if (!user.getId().equals(studyHistory.getUser().getId())) {
            throw new NotOwnedStudyHistory();
        }

        var loc = uploadService.upload(userId, videoFile);
        studyHistory.updateSavedLocation(loc);
        studyHistory.updateThumbnail(loc.replace(".m3u8", ".png"));
        studyHistory.updateVideoInfo(loc.replace(".m3u8", ".json"));
        return studyHistory;
    }

    @Transactional
    public StudyHistory updateStudyHistory(String userId, StudyHistoryDTO.HistoryUpdateRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        StudyHistory studyHistory = studyHistoryRepository.findById(dto.getId()).orElseThrow(NotFoundHistory::new);
        authenticateOwner(user, studyHistory);

        studyHistory.updateHistoryTitle(dto.getHistoryTitle());
        return studyHistory;
    }

    public StudyHistory findStudyHistory(Long id) {
        return studyHistoryRepository.findById(id).orElseThrow(NotFoundStudyHistory::new);
    }

    public List<StudyHistory> findAll(String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return studyHistoryRepository.findAllByUserId(user.getId());
    }

    private void authenticateOwner(User user, StudyHistory studyHistory) {
        if (!user.getId().equals(studyHistory.getUser().getId())) {
            throw new NotOwnedStudyHistory();
        }
    }
}
