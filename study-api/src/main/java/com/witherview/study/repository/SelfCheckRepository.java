package com.witherview.study.repository;

import com.witherview.mysql.entity.SelfCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelfCheckRepository extends JpaRepository<SelfCheck, Long> {
}
