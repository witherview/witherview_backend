package com.witherview.database.repository;

import com.witherview.database.entity.StudyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyFeedbackRepository extends CrudRepository<StudyFeedback, Long> {

    @Query("select avg(f.score) from StudyFeedback f inner join f.receivedUser user where user.id = :userId")
    Optional<Double> getAvgInterviewScoreByUserId(@Param("userId") String userId);

    @Query("select count(f), sum(case f.passOrFail when true then 1 else 0 end) from StudyFeedback f where f.receivedUser.id = :userId")
    List<Object[]> getPassOrFailCountByUserId(@Param("userId") String userId);
}
