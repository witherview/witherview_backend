package com.witherview.groupPractice.GroupStudy;

import com.witherview.database.entity.*;
import com.witherview.database.repository.*;
import com.witherview.groupPractice.exception.*;
import com.witherview.selfPractice.exception.UserNotFoundException;
import com.witherview.utils.GroupStudyMapper;
import com.witherview.utils.StringUtils;
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
@Service
public class GroupStudyService {
    private final GroupStudyMapper groupStudyMapper;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyFeedbackRepository studyFeedbackRepository;
    private final StudyRoomParticipantRepository studyRoomParticipantRepository;
    private final UserRepository userRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final int pageSize = 6;

    public StudyRoom saveRoom(String userId, GroupStudyDTO.StudyCreateDTO requestDto) {
        StudyRoom studyRoom = groupStudyMapper.toStudyRoomEntity(requestDto);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.addHostedRoom(studyRoom);
        return studyRoomRepository.save(studyRoom);
    }

    public void updateRoom(String userId, Long roomId, GroupStudyDTO.StudyUpdateDTO requestDto) {
        StudyRoom studyRoom = findRoom(roomId);

        if(!studyRoom.getHost().getId().equals(userId)) {
            throw new NotStudyRoomHost();
        }
        var localDate = StringUtils.toLocalDate(requestDto.getDate());
        var localTime = StringUtils.toLocalTime(requestDto.getTime());
        studyRoom.update(requestDto.getTitle(), requestDto.getDescription(),
                        requestDto.getIndustry(), requestDto.getJob(),
                        localDate, localTime);
    }

    public StudyRoom deleteRoom(Long id, String userId) {
        StudyRoom studyRoom = findRoom(id);

        if(!studyRoom.getHost().getId().equals(userId)) {
            throw new NotStudyRoomHost();
        }
        studyRoomRepository.delete(studyRoom);
        return studyRoom;
    }

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

    public StudyFeedback createFeedBack(String userId, GroupStudyDTO.StudyFeedBackDTO requestDto) {
        // 해당 스터디룸이 존재하는지 확인
        findRoom(requestDto.getStudyRoomId());

        StudyHistory studyHistory = studyHistoryRepository.findById(requestDto.getHistoryId())
                .orElseThrow(NotFoundStudyHistory::new);;

        User sendUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User receivedUser = userRepository.findById(requestDto.getTargetUser()).orElseThrow(UserNotFoundException::new);

        if( findParticipant(requestDto.getStudyRoomId(), sendUser.getId()) == null) {
            throw new NotJoinedStudyRoom();
        }
        if( findParticipant(requestDto.getStudyRoomId(), receivedUser.getId()) == null) {
            throw new NotCreatedFeedback();
        }
        if( !studyHistory.getUser().getId().equals(receivedUser.getId())) {
            throw new NotOwnedStudyHistory();
        }
        StudyFeedback studyFeedback = StudyFeedback.builder()
                                                    .receivedUser(receivedUser)
                                                    .sendUser(sendUser)
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
        return studyRoomParticipantRepository.findByStudyRoomIdAndUserId(id, userId)
                .orElseThrow(NotJoinedStudyRoom::new);
    }

    public List<GroupStudyDTO.ParticipantDTO> findParticipatedUsers(Long id) {
        StudyRoom studyRoom = findRoom(id);
        return studyRoom.getStudyRoomParticipants()
                .stream()
                .map(r -> {
                    User user = r.getUser();
                    var responseDto = groupStudyMapper.toParticipantDto(user);
                    if (studyRoom.getHost().getId().equals(user.getId())) {
                        responseDto.setIsHost(true);
                    }
                    else {
                        responseDto.setIsHost(false);
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }


    public List<StudyRoom> findRooms(Integer current) {
        int page = current == null ? 0 : current;
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("date", "time").ascending());
        var result = studyRoomRepository.findAll(pageRequest).getContent();
        return result;
    }

    public List<StudyRoom> findCategoryRooms(String category, Integer current) {
        int page = current == null ? 0 : current;
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by("date", "time").ascending());
        var result = studyRoomRepository.findAllByCategory(pageRequest, category).getContent();
        return result;
    }
}
