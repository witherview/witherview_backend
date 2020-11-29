package com.witherview.database.repository;

import com.witherview.database.entity.StudyRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRoomParticipantRepository extends JpaRepository<StudyRoomParticipant, Long> {
    List<StudyRoomParticipant> findByStudyRoomId(Long studyRoomId);

    void deleteByStudyRoomIdAndUserId(Long studyRoomId, Long userId);
}
