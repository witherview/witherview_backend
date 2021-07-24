package com.witherview.mysql.repository;

import com.witherview.mysql.entity.StudyRoom;

import java.util.List;

public interface StudyRoomQDslRepository {
    List<StudyRoom> findRooms(String userId, String job, String keyword, Long lastId, int pageSize);
}
