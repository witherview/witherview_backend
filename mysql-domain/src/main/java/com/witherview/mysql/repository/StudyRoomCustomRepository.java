package com.witherview.mysql.repository;

import com.witherview.mysql.entity.StudyRoom;
import java.util.List;

public interface StudyRoomCustomRepository {
  List<StudyRoom> findRooms(String userId, String industry, String job, String keyword, Long lastId, int pageSize);
}
