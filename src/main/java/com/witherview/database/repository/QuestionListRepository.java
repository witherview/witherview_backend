package com.witherview.database.repository;

import com.witherview.database.entity.QuestionList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionListRepository extends JpaRepository<QuestionList, Long> {
    List<QuestionList> findAllByOwnerId(Long ownerId);
}
