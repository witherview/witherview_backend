package com.witherview.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.witherview.chat.exception.NotSavedFeedBackChat;
import com.witherview.database.entity.FeedBackChat;
import com.witherview.database.entity.StudyHistory;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.User;
import com.witherview.database.repository.FeedBackChatRepository;
import com.witherview.database.repository.StudyHistoryRepository;
import com.witherview.database.repository.StudyRoomRepository;
import com.witherview.database.repository.UserRepository;
import com.witherview.groupPractice.exception.NotFoundStudyHistory;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.groupPractice.exception.NotOwnedStudyHistory;
import com.witherview.selfPractice.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Gson gson;
    private final FeedBackChatRepository feedBackChatRepository;
    private final UserRepository userRepository;
    private final StudyHistoryRepository studyHistoryRepository;

    // 캐싱 용도로 피드백메세지 레디스에 임시 저장
    @Transactional
    public ChatDTO.FeedBackDTO saveRedis(Long studyHistoryId, ChatDTO.FeedBackDTO message) {
        message.setCreatedAt(LocalDateTime.now().toString());

        redisTemplate.opsForList().rightPush(studyHistoryId.toString(), gson.toJson(message));
        return message;
    }

    @Transactional
    public List<FeedBackChat> saveFeedbackMessage(ChatDTO.SaveDTO requestDto) {
        String historyId = requestDto.getStudyHistoryId().toString();

        List<FeedBackChat> lists =  redisTemplate.opsForList().range(historyId, 0, -1)
                .stream()
                .map(message -> {
                    try{
                        ChatDTO.FeedBackDTO dto =  objectMapper.readValue(message.toString(), ChatDTO.FeedBackDTO.class);

                        User writtenUser = findUser(dto.getWrittenUserId());
                        User targetUser = findUser(dto.getTargetUserId());
                        StudyHistory studyHistory = findStudyHistory(dto.getStudyHistoryId());

                        FeedBackChat feedBackChat = FeedBackChat.builder()
                                .studyHistory(studyHistory)
                                .writtenUser(writtenUser)
                                .targetUser(targetUser)
                                .message(dto.getMessage())
                                .createdAt(LocalDateTime.parse(dto.getCreatedAt()))
                                .build();

                        return feedBackChatRepository.save(feedBackChat);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new NotSavedFeedBackChat();
                    }
                })
                .collect(Collectors.toList());

        redisTemplate.delete(historyId);
        return lists;
    }

    public List<FeedBackChat> getFeedbackMessage(Long historyId, Long userId, Integer idx) {
        StudyHistory studyHistory = findStudyHistory(historyId);
        if(studyHistory.getUser().getId() != userId) throw new NotOwnedStudyHistory();

        int page = idx == null ? 0 : idx;
        Pageable pageRequest = PageRequest.of(page, 10, Sort.by("createdAt").ascending());
        return feedBackChatRepository.findAllByStudyHistoryId(pageRequest, historyId);
    }

    public StudyHistory findStudyHistory(Long id) {
        return studyHistoryRepository.findById(id).orElseThrow(NotFoundStudyHistory::new);
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundUser::new);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
