package com.witherview.database.repository;

import com.witherview.database.entity.SelfCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfCheckRepository extends JpaRepository<SelfCheck, Long> {
}
