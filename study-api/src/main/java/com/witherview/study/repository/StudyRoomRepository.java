package com.witherview.study.repository;

import com.witherview.mysql.entity.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long>, StudyRoomCustomRepository {
}