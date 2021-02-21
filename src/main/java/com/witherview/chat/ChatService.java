package com.witherview.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.witherview.database.entity.FeedBackChat;
import com.witherview.database.entity.StudyHistory;
import com.witherview.database.repository.FeedBackChatRepository;
import com.witherview.database.repository.StudyHistoryRepository;
import com.witherview.groupPractice.exception.NotFoundStudyHistory;
import com.witherview.groupPractice.exception.NotOwnedStudyHistory;
import com.witherview.utils.FeedBackMapper;
import com.witherview.utils.StreamExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final FeedBackMapper feedBackMapper;
    private final Gson gson;
    private final FeedBackChatRepository feedBackChatRepository;
    private final StudyHistoryRepository studyHistoryRepository;

    // 캐싱 용도로 피드백메세지 레디스에 임시 저장
    public ChatDTO.FeedBackDTO saveRedis(Long studyHistoryId, ChatDTO.FeedBackDTO message) {
        redisTemplate.opsForList().rightPush(studyHistoryId.toString(), gson.toJson(message));
        return message;
    }
    
    public List<ChatDTO.FeedBackDTO> saveFeedbackMessage(ChatDTO.SaveDTO requestDto) {
        List<ChatDTO.FeedBackDTO> resultList = new ArrayList<>();
        String historyId = requestDto.getStudyHistoryId().toString();

        Iterable<FeedBackChat> lists =  redisTemplate.opsForList().range(historyId, 0, -1)
                .stream()
                .map(streamMapper(
                        message -> {
                          resultList.add(objectMapper.readValue((String) message, ChatDTO.FeedBackDTO.class));
                          return objectMapper.readValue(
                                  (String) message, FeedBackChat.class);
                        })
                )
                .collect(Collectors.toList());
        // 한번에 저장
        feedBackChatRepository.insert(lists);
        redisTemplate.delete(historyId);
        return resultList;
    }

    public List<ChatDTO.FeedBackDTO> getFeedbackMessageByReceivedUserId(Long userId, Long historyId, Integer idx) {
        StudyHistory studyHistory = findStudyHistory(historyId);
        if(studyHistory.getUser().getId() != userId) throw new NotOwnedStudyHistory();

        int page = idx == null ? 0 : idx;
        Pageable pageRequest = PageRequest.of(page, 10,
                Sort.by("studyHistoryId").ascending()
        );
        var result = feedBackChatRepository.findAllByReceivedUserIdAndStudyHistoryId(userId, historyId, pageRequest);
        var content = result.getContent();
        return content.stream().map(chatEntity -> feedBackMapper.toDto(chatEntity)).collect(Collectors.toList());
    }

    public List<ChatDTO.FeedBackDTO> getFeedbackMessageByWrittenUserIdAndTargetUser(Long userId, Long historyId, Integer idx) {
        StudyHistory studyHistory = findStudyHistory(historyId);
        if(studyHistory.getUser().getId() != userId) throw new NotOwnedStudyHistory();

        int page = idx == null ? 0 : idx;
        Pageable pageRequest = PageRequest.of(page, 10,
                Sort.by("studyHistoryId").ascending()
        );
        var result = feedBackChatRepository.findAllByReceivedUserIdAndStudyHistoryId(userId, historyId, pageRequest);
        var content = result.getContent();
        return content.stream().map(chatEntity -> feedBackMapper.toDto(chatEntity)).collect(Collectors.toList());
    }
    
    public StudyHistory findStudyHistory(Long id) {
        return studyHistoryRepository.findById(id).orElseThrow(NotFoundStudyHistory::new);
    }
    
    private <T,R> Function<T,R> streamMapper(StreamExceptionHandler<T, R> f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (Exception e) {
                // todo: 원래 발생하는 Exception은 JsonProcessingException
                throw new RuntimeException(e);
            }
        };
    }
}
