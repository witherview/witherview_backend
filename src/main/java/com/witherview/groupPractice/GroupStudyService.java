package com.witherview.groupPractice;

import com.witherview.database.entity.*;
import com.witherview.database.repository.*;
import com.witherview.groupPractice.exception.*;
import com.witherview.selfPractice.exception.NotFoundUser;
import com.witherview.video.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GroupStudyService {

    private final StudyRoomRepository studyRoomRepository;
    private final StudyFeedbackRepository studyFeedbackRepository;
    private final StudyRoomParticipantRepository studyRoomParticipantRepository;
    private final UserRepository userRepository;
    private final StudyVideoRepository studyVideoRepository;
    private final VideoService videoService;
    private final int pageSize = 6;

    @Transactional
    public StudyRoom saveRoom(Long userId, GroupStudyDTO.StudyCreateDTO requestDto) {
        StudyRoom studyRoom = requestDto.toEntity();
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);

        user.addHostedRoom(studyRoom);
        return studyRoomRepository.save(studyRoom);
    }

    @Transactional
    public void updateRoom(Long userId, GroupStudyDTO.StudyUpdateDTO requestDto) {
        StudyRoom studyRoom = findRoom(requestDto.getId());

        if(studyRoom.getHost().getId() != userId) {
            throw new NotStudyRoomHost();
        }
        studyRoom.update(requestDto.getTitle(), requestDto.getDescription(),
                        requestDto.getIndustry(), requestDto.getJob(),
                        requestDto.getDate(), requestDto.getTime());
    }

    @Transactional
    public StudyRoom deleteRoom(Long id, Long userId) {
        StudyRoom studyRoom = findRoom(id);

        if(studyRoom.getHost().getId() != userId) {
            throw new NotStudyRoomHost();
        }
        studyRoomRepository.delete(studyRoom);
        return studyRoom;
    }

    @Transactional
    public StudyRoom joinRoom(Long id, Long userId) {
        // 이미 참여하고 있는 방인 경우
        if(findParticipant(id, userId) != null) {
            throw new AlreadyJoinedStudyRoom();
        }

        StudyRoom studyRoom = findRoom(id);
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        StudyRoomParticipant studyRoomParticipant = StudyRoomParticipant.builder()
                                                                        .studyRoom(studyRoom)
                                                                        .user(user)
                                                                        .build();
        studyRoom.addParticipants(studyRoomParticipant);
        studyRoom.increaseNowUserCnt();
        user.addParticipatedRoom(studyRoomParticipant);
        studyRoomParticipantRepository.save(studyRoomParticipant);
        return studyRoom;
    }

    @Transactional
    public StudyRoom leaveRoom(Long id, Long userId) {
        // 참여하지 않은 방인 경우
        if(findParticipant(id, userId) == null) {
            throw new NotJoinedStudyRoom();
        }
        StudyRoom studyRoom = findRoom(id);
        studyRoom.decreaseNowUserCnt();
        studyRoomParticipantRepository.deleteByStudyRoomIdAndUserId(id, userId);
        return studyRoom;
    }

    @Transactional
    public StudyFeedback createFeedBack(Long userId, GroupStudyDTO.StudyFeedBackDTO requestDto) {
        StudyRoom studyRoom = findRoom(requestDto.getId());
        User writtenUser = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        User targetUser = userRepository.findById(requestDto.getTargetUser()).orElseThrow(NotFoundUser::new);

        if(findParticipant(requestDto.getId(), writtenUser.getId()) == null) {
            throw new NotJoinedStudyRoom();
        }
        if(findParticipant(requestDto.getId(), targetUser.getId()) == null) {
            throw new NotCreatedFeedback();
        }
        StudyFeedback studyFeedback = StudyFeedback.builder()
                                                    .studyRoom(studyRoom)
                                                    .writtenUser(writtenUser)
                                                    .score(requestDto.getScore())
                                                    .passOrFail(requestDto.getPassOrFail())
                                                    .build();

        targetUser.addStudyFeedback(studyFeedback);
        return studyFeedbackRepository.save(studyFeedback);
    }

    @Transactional
    public StudyVideo uploadVideo(MultipartFile videoFile, Long studyRoomId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        findRoom(studyRoomId);
        StudyVideo studyVideo = new StudyVideo();
        String savedLocation = videoService.upload(videoFile,
                user.getEmail() + "/group/" + studyRoomId);
        user.addStudyVideo(studyVideo);
        studyVideo.updateSavedLocation(savedLocation);
        return studyVideoRepository.save(studyVideo);
    }

    public StudyRoom findRoom(Long id) {
        return studyRoomRepository.findById(id)
                .orElseThrow(NotFoundStudyRoom::new);
    }

    public StudyRoomParticipant findParticipant(Long id, Long userId) {
        return studyRoomParticipantRepository.findByStudyRoomIdAndUserId(id, userId);
    }

    public List<User> findParticipatedUsers(Long id) {
        return findRoom(id).getStudyRoomParticipants()
                .stream()
                .map(r -> r.getUser())
                .collect(Collectors.toList());
    }

    public List<StudyRoom> findParticipatedRooms(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        return user.getParticipatedStudyRooms()
                    .stream()
                    .map(r -> r.getStudyRoom())
                    .collect(Collectors.toList());
    }

    public List<StudyRoom> findRooms(Integer current) {
        int page = current == null ? 0 : current;
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("date", "time").ascending());

        return studyRoomRepository.findAll(pageRequest).getContent();
    }
}
