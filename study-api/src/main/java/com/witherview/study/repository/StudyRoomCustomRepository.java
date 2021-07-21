package com.witherview.study.repository;

import com.witherview.mysql.entity.StudyRoom;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomCustomRepository {
  List<StudyRoom> findRooms(String userId, String job, String keyword, Long lastId, int pageSize);
}
