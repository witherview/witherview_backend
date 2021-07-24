package com.witherview.study.service;

import com.witherview.mysql.entity.StudyHistory;
import com.witherview.mysql.entity.User;

import com.witherview.mysql.repository.StudyHistoryRepository;
import com.witherview.mysql.repository.StudyRoomParticipantRepository;
import com.witherview.mysql.repository.StudyRoomRepository;
import com.witherview.mysql.repository.UserRepository;
//import com.witherview.study.repository.StudyHistoryRepository;
//import com.witherview.study.repository.StudyRoomParticipantRepository;
//import com.witherview.study.repository.StudyRoomRepository;
//import com.witherview.study.repository.UserRepository;
import exception.study.NotFoundStudyHistory;
import exception.study.NotFoundStudyRoom;
import exception.study.NotJoinedStudyRoom;
import exception.study.NotOwnedStudyHistory;
import exception.study.UserNotFoundException;
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
    private final VideoService videoService;

    @Transactional
    public StudyHistory uploadVideo(MultipartFile videoFile, Long historyId, String userId) {
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
    public StudyHistory saveStudyHistory(Long studyRoomId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        studyRoomRepository.findById(studyRoomId).orElseThrow(NotFoundStudyRoom::new);
        var studyRoomParticipant = studyRoomParticipantRepository
                .findByStudyRoomIdAndUserId(studyRoomId, userId).orElseThrow(NotJoinedStudyRoom::new);

        StudyHistory studyHistory = StudyHistory.builder().studyRoom(studyRoomId).build();
        user.addStudyHistory(studyHistory);
        user.increaseGroupPracticeCnt();
        return studyHistoryRepository.save(studyHistory);
    }

    public StudyHistory findStudyHistory(Long id) {
        return studyHistoryRepository.findById(id).orElseThrow(NotFoundStudyHistory::new);
    }
}
