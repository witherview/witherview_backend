package com.witherview.database.repository;

import com.witherview.database.entity.FeedBackChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackChatRepository extends MongoRepository<FeedBackChat, String> {
    // 조회자 본인 + 해당 면접에서의 모든 피드백 조회하기
    Page<FeedBackChat> findAllByReceivedUserIdAndStudyHistoryId(
            String receivedUserId,
            Long studyHistoryId, Pageable pagable);
    // 조회자 본인 + 해당 면접의 특정 사람 피드백 조회하기
    Page<FeedBackChat> findAllByReceivedUserIdAndStudyHistoryIdAndSendUserId(
            String receivedUserId,
            Long studyHistoryId,
            String sendUserId, Pageable pagable);

    // TODO: 이동건. 메소드 대체 필요.
    List<FeedBackChat> findAllByStudyHistoryId(Pageable pageable, Long historyId);
}
