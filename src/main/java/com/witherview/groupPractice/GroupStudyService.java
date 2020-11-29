package com.witherview.groupPractice;

import com.witherview.database.entity.StudyFeedback;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.StudyRoomParticipant;
import com.witherview.database.entity.User;
import com.witherview.database.repository.StudyFeedbackRepository;
import com.witherview.database.repository.StudyRoomParticipantRepository;
import com.witherview.database.repository.StudyRoomRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.selfPractice.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
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

    @Transactional
    public StudyRoom saveRoom(Long userId, GroupStudyDTO.StudyCreateDTO requestDto) {
        StudyRoom studyRoom = requestDto.toEntity();
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);

        user.addHostedRoom(studyRoom);

        return studyRoomRepository.save(studyRoom);
    }

    @Transactional
    public void updateRoom(GroupStudyDTO.StudyUpdateDTO requestDto) {
        StudyRoom studyRoom = findRoom(requestDto.getId());
        studyRoom.update(requestDto.getTitle(), requestDto.getDescription(),
                        requestDto.getIndustry(), requestDto.getJob(),
                        requestDto.getDate(), requestDto.getTime());
    }

    @Transactional
    public StudyRoom deleteRoom(Long id) {
        StudyRoom studyRoom = findRoom(id);
        studyRoomRepository.delete(studyRoom);
        return studyRoom;
    }

    @Transactional
    public StudyRoom joinRoom(Long id, Long userId) {
        StudyRoom studyRoom = findRoom(id);
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        StudyRoomParticipant studyRoomParticipant = StudyRoomParticipant.builder()
                                                                        .studyRoom(studyRoom)
                                                                        .user(user)
                                                                        .build();

        studyRoom.addParticipants(studyRoomParticipant);
        user.addParticipatedRoom(studyRoomParticipant);
        return studyRoom;
    }

    @Transactional
    public StudyRoom leaveRoom(Long id, Long userId) {
        StudyRoom studyRoom = findRoom(id);
        studyRoomParticipantRepository.deleteByStudyRoomIdAndUserId(id, userId);
        return studyRoom;
    }

    @Transactional
    public StudyFeedback createFeedBack(Long userId, GroupStudyDTO.StudyFeedBackDTO requestDto) {
        StudyRoom studyRoom = findRoom(requestDto.getId());
        User writtenUser = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        User targetUser = userRepository.findById(requestDto.getTargetUser()).orElseThrow(NotFoundUser::new);

        StudyFeedback studyFeedback = StudyFeedback.builder()
                                                    .studyRoom(studyRoom)
                                                    .writtenUser(writtenUser)
                                                    .score(requestDto.getScore())
                                                    .passOrFail(requestDto.getPassOrFail())
                                                    .build();

        targetUser.addStudyFeedback(studyFeedback);

        return studyFeedbackRepository.save(studyFeedback);
    }

    public List<User> findParticipants(Long id) {
        return findRoom(id).getStudyRoomParticipants()
                            .stream()
                            .map(r -> r.getUser())
                            .collect(Collectors.toList());
    }

    public StudyRoom findRoom(Long id) {
        return studyRoomRepository.findById(id)
                .orElseThrow(NotFoundStudyRoom::new);
    }

    public List<StudyRoom> findParticipatedRooms(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUser::new);
        return user.getParticipatedStudyRooms()
                    .stream()
                    .map(r -> r.getStudyRoom())
                    .collect(Collectors.toList());
    }

    public List<StudyRoom> findAllRooms() {
        List<StudyRoom> lists = studyRoomRepository.findAll();
        // 면접 날짜 가까운 순으로 정렬
        Collections.sort(lists, new Comparator<StudyRoom>() {
            @Override
            public int compare(StudyRoom o1, StudyRoom o2) {
                if(o1.getDate().isEqual(o2.getDate())) {
                    return o2.getTime().isBefore(o1.getTime()) ? 1 : -1;
                }
                return o2.getDate().isBefore(o1.getDate()) ? 1 : -1;
            }
        });
        return lists;
    }
}
