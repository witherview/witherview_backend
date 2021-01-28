package com.witherview.database.repository;

import com.witherview.database.entity.StudyRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    List<StudyRoom> findAllByCategory(Pageable pageable, String category);
}
