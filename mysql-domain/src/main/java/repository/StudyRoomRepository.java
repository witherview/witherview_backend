package repository;

import entity.StudyRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    Page<StudyRoom> findAllByCategory(Pageable pageable, String category);
}
