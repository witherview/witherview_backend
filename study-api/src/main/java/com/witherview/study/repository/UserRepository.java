package com.witherview.study.repository;

import com.witherview.mysql.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    public Optional<User> findByEmail(String email);
}
