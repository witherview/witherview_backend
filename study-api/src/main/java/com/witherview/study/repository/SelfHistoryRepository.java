package com.witherview.study.repository;

import com.witherview.mysql.entity.SelfHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfHistoryRepository extends JpaRepository<SelfHistory, Long> {
    List<SelfHistory> findAllByUserId(String userId);
}
