package com.witherview.study.repository;

import com.witherview.mysql.entity.CheckListType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListTypeRepository extends JpaRepository<CheckListType, Long> {
}
