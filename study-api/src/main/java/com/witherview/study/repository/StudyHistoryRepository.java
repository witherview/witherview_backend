package com.witherview.study.repository;

import com.witherview.mysql.entity.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyHistoryRepository extends JpaRepository<StudyHistory, Long> {
}
