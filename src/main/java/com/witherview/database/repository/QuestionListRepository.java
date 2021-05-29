package com.witherview.database.repository;

import com.witherview.database.entity.QuestionList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionListRepository extends CrudRepository<QuestionList, Long> {

    List<QuestionList> findAllByUserId(String userId);

    @Query("select count(q) from QuestionList q where q.userId = :ownerId")
    Long CountByOwnerId(@Param("ownerId") String ownerId);

    List<QuestionList> findFirstByUserId(String userId);

    @Query("select q from QuestionList q where q.userId = :userId and q.id = :id")
    List<QuestionList> findByUserIdAndId(@Param("userId") String userId, @Param("id") Long id);
}