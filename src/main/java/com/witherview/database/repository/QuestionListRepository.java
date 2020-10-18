package com.witherview.database.repository;

import com.witherview.database.entity.QuestionList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionListRepository extends JpaRepository<QuestionList, Long> {
}
