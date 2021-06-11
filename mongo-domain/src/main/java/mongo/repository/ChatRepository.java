package mongo.repository;

import mongo.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, Long> {
    Page<Chat> findAllByStudyRoomId(Long studyRoomId, Pageable pagable);
}
