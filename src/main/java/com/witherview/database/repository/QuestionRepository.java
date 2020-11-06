package com.witherview.database.repository;

import com.witherview.database.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByBelongListId(Long belongListId);
}
