package com.witherview.study.repository;

import com.witherview.mysql.entity.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long>, StudyRoomCustomRepository {
}