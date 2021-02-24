package com.witherview.database.repository;

import com.witherview.database.entity.StudyRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.LongSummaryStatistics;

public interface StudyRoomParticipantRepository extends JpaRepository<StudyRoomParticipant, Long> {
    StudyRoomParticipant findByStudyRoomIdAndUserId(Long studyRoomId, String userId);

    void deleteByStudyRoomIdAndUserId(Long studyRoomId, String userId);
}
