package com.witherview.database.repository;

import com.witherview.database.entity.QuestionList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionListRepository extends CrudRepository<QuestionList, Long> {
    // todo: Long이 맞는가?
    List<QuestionList> findAllByOwnerId(Long ownerId);

    @Query("select count(q) from QuestionList q where q.owner.id = :ownerId")
    Long CountByOwnerId(@Param("ownerId") String ownerId);
}