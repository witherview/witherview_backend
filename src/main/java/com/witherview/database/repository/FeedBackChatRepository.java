package com.witherview.database.repository;

import com.witherview.database.entity.FeedBackChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedBackChatRepository extends JpaRepository<FeedBackChat, Long> {
    List<FeedBackChat> findAllByStudyHistoryId(Pageable pageable, Long historyId);
}
