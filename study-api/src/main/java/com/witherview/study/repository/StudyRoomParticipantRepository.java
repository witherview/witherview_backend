package com.witherview.study.repository;

import com.witherview.mysql.entity.StudyRoomParticipant;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface StudyRoomParticipantRepository extends CrudRepository<StudyRoomParticipant, Long> {
    Optional<StudyRoomParticipant> findByStudyRoomIdAndUserId(Long studyRoomId, String userId);

    void deleteByStudyRoomIdAndUserId(Long studyRoomId, String userId);

}
