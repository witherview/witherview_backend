package repository;

import entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {
}
