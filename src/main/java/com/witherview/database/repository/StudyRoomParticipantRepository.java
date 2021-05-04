package com.witherview.database.repository;

import com.witherview.database.entity.StudyRoomParticipant;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudyRoomParticipantRepository extends CrudRepository<StudyRoomParticipant, Long> {
    Optional<StudyRoomParticipant> findByStudyRoomIdAndUserId(Long studyRoomId, String userId);

    void deleteByStudyRoomIdAndUserId(Long studyRoomId, String userId);

}
