package com.witherview.database.repository;

import com.witherview.database.entity.SelfStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelfStudyRepository extends JpaRepository<SelfStudy, Long> {
    List<SelfStudy> findAllByUserId(Long userId);
}
