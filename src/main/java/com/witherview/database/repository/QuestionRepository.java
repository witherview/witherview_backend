package com.witherview.database.repository;

import com.witherview.database.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByBelongListId(Long belongListId);

    @Query("select count(q) from QuestionList q where q.userId = :ownerId")
    Long CountByOwnerId(@Param("ownerId") String ownerId);

}
