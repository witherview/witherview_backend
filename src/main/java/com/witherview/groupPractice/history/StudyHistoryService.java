package com.witherview.groupPractice.history;

import com.witherview.database.entity.StudyHistory;
import com.witherview.database.entity.StudyRoomParticipant;
import com.witherview.database.entity.User;
import com.witherview.database.repository.*;
import com.witherview.groupPractice.exception.NotFoundStudyHistory;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.groupPractice.exception.NotJoinedStudyRoom;
import com.witherview.groupPractice.exception.NotOwnedStudyHistory;
import com.witherview.selfPractice.exception.UserNotFoundException;
import com.witherview.video.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StudyHistoryService {

    private final UserRepository userRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyRoomParticipantRepository studyRoomParticipantRepository;
    private final VideoService videoService;

    @Transactional
    public StudyHistory uploadVideo(MultipartFile videoFile, Long historyId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        StudyHistory studyHistory = findStudyHistory(historyId);

        if (!user.getId().equals(studyHistory.getUser().getId())) {
            throw new NotOwnedStudyHistory();
        }
        String savedLocation = videoService.upload(videoFile,
                user.getEmail() + "/group/" + historyId);
        studyHistory.updateSavedLocation(savedLocation);
        return studyHistory;
    }

    @Transactional
    public StudyHistory saveStudyHistory(Long studyRoomId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        studyRoomRepository.findById(studyRoomId).orElseThrow(NotFoundStudyRoom::new);
        StudyRoomParticipant studyRoomParticipant = studyRoomParticipantRepository
                .findByStudyRoomIdAndUserId(studyRoomId, userId);

        if (studyRoomParticipant == null) {
            throw new NotJoinedStudyRoom();
        }

        StudyHistory studyHistory = StudyHistory.builder().studyRoom(studyRoomId).build();
        user.addStudyHistory(studyHistory);
        user.increaseGroupPracticeCnt();
        return studyHistoryRepository.save(studyHistory);
    }

    public StudyHistory findStudyHistory(Long id) {
        return studyHistoryRepository.findById(id).orElseThrow(NotFoundStudyHistory::new);
    }
}
