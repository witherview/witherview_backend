package com.witherview.mysql.repository;

import com.witherview.mysql.entity.SelfHistory;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelfHistoryRepository extends JpaRepository<SelfHistory, Long> {
    List<SelfHistory> findAllByUserId(String userId, Pageable pageable);
}
