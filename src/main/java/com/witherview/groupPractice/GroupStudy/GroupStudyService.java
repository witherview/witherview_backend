package com.witherview.groupPractice.GroupStudy;

import com.witherview.database.entity.*;
import com.witherview.database.repository.*;
import com.witherview.groupPractice.exception.*;
import com.witherview.selfPractice.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final StudyHistoryRepository studyHistoryRepository;
    private final int pageSize = 6;

    @Transactional
    public StudyRoom saveRoom(String userId, GroupStudyDTO.StudyCreateDTO requestDto) {
        StudyRoom studyRoom = requestDto.toEntity();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        user.addHostedRoom(studyRoom);
        return studyRoomRepository.save(studyRoom);
    }

    @Transactional
    public void updateRoom(String userId, GroupStudyDTO.StudyUpdateDTO requestDto) {
        StudyRoom studyRoom = findRoom(requestDto.getId());

        if(studyRoom.getHost().getId() != userId) {
            throw new NotStudyRoomHost();
        }
        studyRoom.update(requestDto.getTitle(), requestDto.getDescription(),
                        requestDto.getIndustry(), requestDto.getJob(),
                        requestDto.getDate(), requestDto.getTime());
    }

    @Transactional
    public StudyRoom deleteRoom(Long id, String userId) {
        StudyRoom studyRoom = findRoom(id);

        if(studyRoom.getHost().getId() != userId) {
            throw new NotStudyRoomHost();
        }
        studyRoomRepository.delete(studyRoom);
        return studyRoom;
    }

    @Transactional
    public StudyRoom joinRoom(Long id, String userId) {
        // 이미 참여하고 있는 방인 경우
        if(findParticipant(id, userId) != null) {
            throw new AlreadyJoinedStudyRoom();
        }

        StudyRoom studyRoom = findRoom(id);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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
    public StudyRoom leaveRoom(Long id, String userId) {
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
    public StudyFeedback createFeedBack(String userId, GroupStudyDTO.StudyFeedBackDTO requestDto) {
        findRoom(requestDto.getStudyRoomId());
        StudyHistory studyHistory = studyHistoryRepository.findById(requestDto.getHistoryId())
                .orElseThrow(NotFoundStudyHistory::new);;

        User writtenUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(requestDto.getTargetUser()).orElseThrow(UserNotFoundException::new);

        if(findParticipant(requestDto.getStudyRoomId(), writtenUser.getId()) == null) {
            throw new NotJoinedStudyRoom();
        }
        if(findParticipant(requestDto.getStudyRoomId(), targetUser.getId()) == null) {
            throw new NotCreatedFeedback();
        }
        if(studyHistory.getUser().getId() != targetUser.getId()) {
            throw new NotOwnedStudyHistory();
        }
        StudyFeedback studyFeedback = StudyFeedback.builder()
                                                    .targetUser(targetUser)
                                                    .writtenUser(writtenUser)
                                                    .score(requestDto.getScore())
                                                    .passOrFail(requestDto.getPassOrFail())
                                                    .build();

        studyHistory.addStudyFeedBack(studyFeedback);
        return studyFeedbackRepository.save(studyFeedback);
    }

    public StudyRoom findRoom(Long id) {
        return studyRoomRepository.findById(id)
                .orElseThrow(NotFoundStudyRoom::new);
    }

    public StudyRoomParticipant findParticipant(Long id, String userId) {
        return studyRoomParticipantRepository.findByStudyRoomIdAndUserId(id, userId);
    }

    public List<GroupStudyDTO.ParticipantDTO> findParticipatedUsers(Long id) {
        StudyRoom studyRoom = findRoom(id);
        ModelMapper modelMapper = new ModelMapper();

        return studyRoom.getStudyRoomParticipants()
                .stream()
                .map(r -> {
                    User user = r.getUser();
                    GroupStudyDTO.ParticipantDTO responseDto = modelMapper.map(user, GroupStudyDTO.ParticipantDTO.class);

                    if(studyRoom.getHost().getId() == user.getId()) responseDto.setIsHost(true);
                    else responseDto.setIsHost(false);

                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    public List<StudyRoom> findParticipatedRooms(String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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

    public List<StudyRoom> findCategoryRooms(String category, Integer current) {
        int page = current == null ? 0 : current;
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("date", "time").ascending());

        return studyRoomRepository.findAllByCategory(pageRequest, category);
    }
}
