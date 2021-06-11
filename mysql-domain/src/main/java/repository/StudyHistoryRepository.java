package repository;

import entity.StudyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyHistoryRepository extends JpaRepository<StudyHistory, Long> {
}
