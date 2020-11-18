package com.witherview.database.repository;

import com.witherview.database.entity.SelfHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelfHistoryRepository extends JpaRepository<SelfHistory, Long> {
    List<SelfHistory> findAllByUserId(Long userId);
}
