package com.witherview.study.repository;

import com.witherview.mysql.entity.StudyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyVideoRepository extends JpaRepository<StudyVideo, Long> {
}
