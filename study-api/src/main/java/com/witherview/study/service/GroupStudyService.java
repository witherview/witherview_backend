package com.witherview.study.service;

import com.witherview.mysql.entity.StudyFeedback;
import com.witherview.mysql.entity.StudyHistory;
import com.witherview.mysql.entity.StudyRoom;
import com.witherview.mysql.entity.StudyRoomParticipant;
import com.witherview.mysql.entity.User;
import com.witherview.mysql.repository.StudyFeedbackRepository;
import com.witherview.mysql.repository.StudyHistoryRepository;
import com.witherview.mysql.repository.StudyRoomParticipantRepository;
import com.witherview.mysql.repository.StudyRoomRepository;
import com.witherview.mysql.repository.UserRepository;
import com.witherview.study.dto.GroupStudyDTO;
import com.witherview.study.mapper.GroupStudyMapper;
import exception.study.AlreadyJoinedStudyRoom;
import exception.study.HostNotLeaveStudyRoom;
import exception.study.NotCreatedFeedback;
import exception.study.NotFoundStudyHistory;
import exception.study.NotFoundStudyRoom;
import exception.study.NotJoinedStudyRoom;
import exception.study.NotOwnedStudyHistory;
import exception.study.NotStudyRoomHost;
import exception.study.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public StudyRoom saveRoom(String userId, GroupStudyDTO.StudyCreateDTO requestDto) {
        StudyRoom studyRoom = groupStudyMapper.toStudyRoomEntity(requestDto);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        studyRoom.updateHost(user);
        return studyRoomRepository.save(studyRoom);
    }

    @Transactional
    public void updateRoom(String userId, Long roomId, GroupStudyDTO.StudyUpdateDTO requestDto) {
        StudyRoom studyRoom = findRoom(roomId);

        if(!studyRoom.getHost().getId().equals(userId)) {
            throw new NotStudyRoomHost();
        }

        studyRoom.update(requestDto.getTitle(),
                requestDto.getDescription(), requestDto.getCategory(),
                requestDto.getIndustry(), requestDto.getJob(),
                requestDto.getDate(), requestDto.getTime());
    }

    @Transactional
    public StudyRoom deleteRoom(Long id, String userId) {
        StudyRoom studyRoom = findRoom(id);

        if(!studyRoom.getHost().getId().equals(userId)) {
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
        StudyRoom studyRoom = findRoom(id);

        // 참여하지 않은 방인 경우
        if(findParticipant(id, userId) == null) {
            throw new NotJoinedStudyRoom();
        }
        // 나가는 사람이 호스트인경우 -> 나갈 수 없음 (호스트 넘겨줘야함)
        if(studyRoom.getHost().getId().equals(userId)) {
            throw new HostNotLeaveStudyRoom();
        }

        studyRoomParticipantRepository.deleteByStudyRoomIdAndUserId(id, userId);
        // 아무도 없는 방인 경우 -> 삭제
        if(studyRoom.decreaseNowUserCnt() == 0) studyRoomRepository.delete(studyRoom);
        return studyRoom;
    }

    @Transactional
    public StudyFeedback createFeedBack(String userId, GroupStudyDTO.StudyFeedBackDTO requestDto) {
        // 해당 스터디룸이 존재하는지 확인
        findRoom(requestDto.getStudyRoomId());

        StudyHistory studyHistory = studyHistoryRepository.findById(requestDto.getStudyHistoryId())
                .orElseThrow(NotFoundStudyHistory::new);;

        User sendUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User receivedUser = userRepository.findById(requestDto.getReceivedUser()).orElseThrow(UserNotFoundException::new);

        if(findParticipant(requestDto.getStudyRoomId(), sendUser.getId()) == null) {
            throw new NotJoinedStudyRoom();
        }
        if(findParticipant(requestDto.getStudyRoomId(), receivedUser.getId()) == null) {
            throw new NotCreatedFeedback();
        }
        if(!studyHistory.getUser().getId().equals(receivedUser.getId())) {
            throw new NotOwnedStudyHistory();
        }
        StudyFeedback studyFeedback = StudyFeedback.builder()
                                                    .receivedUser(receivedUser.getId())
                                                    .sendUser(sendUser.getId())
                                                    .score(requestDto.getScore())
                                                    .passOrFail(requestDto.getPassOrFail())
                                                    .build();

        studyHistory.addStudyFeedBack(studyFeedback);
        return studyFeedbackRepository.save(studyFeedback);
    }

    @Transactional
    public StudyRoom changeRoomHost(Long roomId, String oriHostId, String newHostId) {
        StudyRoom studyRoom = findRoom(roomId);

        // 바꿀 호스트가 해당 스터디룸 참가자인지 체크
        if(findParticipant(roomId, newHostId) == null) {
            throw new NotJoinedStudyRoom();
        }
        // 바꾸려고 하는 사람이 호스트인지 체크
        if(!studyRoom.getHost().getId().equals(oriHostId)) throw new NotStudyRoomHost();

        User newHost = userRepository.findById(newHostId).orElseThrow(UserNotFoundException::new);
        studyRoom.updateHost(newHost);

        return studyRoom;
    }

    public StudyRoom findRoom(Long id) {
        return studyRoomRepository.findById(id)
                .orElseThrow(NotFoundStudyRoom::new);
    }

    public StudyRoomParticipant findParticipant(Long id, String userId) {
        return studyRoomParticipantRepository.findByStudyRoomIdAndUserId(id, userId).orElse(null);
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

    /* 1. 로그인한 사용자가 현재 참여한 방 (첫페이지에 무조건 포함)
       2. 로그인한 사용자가 현재 참여 가능한 방 */
    public List<StudyRoom> findRooms(String userId, String job, String keyword, Long lastId) {
        List<StudyRoom> lists = new ArrayList<>();

        // 첫페이지 -> 참여한 방 포함해서 보여주기
        if(lastId == null) {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            lists = user.getParticipatedStudyRooms()
                .stream().map(e -> e.getStudyRoom()).collect(Collectors.toList());
        }

        var result = studyRoomRepository.findRooms(userId, job, keyword, lastId, pageSize);
        lists.addAll(result);
        return lists;
    }
}
