package repository;


import entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByBelongListId(Long belongListId);

    @Query("select count(q) from QuestionList q where q.userId = :ownerId")
    Long CountByOwnerId(@Param("ownerId") String ownerId);

}
