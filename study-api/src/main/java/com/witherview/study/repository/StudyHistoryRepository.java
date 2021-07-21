package com.witherview.study.repository;

import com.witherview.mysql.entity.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyHistoryRepository extends JpaRepository<StudyHistory, Long> {
}
