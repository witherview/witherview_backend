package com.witherview.database.repository;

import com.witherview.database.entity.StudyFeedback;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyFeedbackRepository extends CrudRepository<StudyFeedback, Long> {

    @Query("select avg(f.score) from StudyFeedback f where f.receivedUser = :id")
    Optional<Double> getAvgInterviewScoreById(@Param("id") String id);

    @Query("select count(f), sum(case f.passOrFail when true then 1 else 0 end) from StudyFeedback f where f.receivedUser = :id")
    List<Object[]> getPassOrFailCountById(@Param("id") String id);
}
