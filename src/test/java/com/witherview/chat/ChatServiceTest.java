package com.witherview.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.witherview.database.entity.FeedBackChat;
import com.witherview.database.entity.StudyHistory;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.repository.FeedBackChatRepository;
import com.witherview.database.repository.StudyHistoryRepository;
import com.witherview.utils.FeedBackMapper;
import com.witherview.utils.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatServiceTest {
    @MockBean
    private StudyHistoryRepository studyHistoryRepository;

    @Autowired
    private ChatService chatService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private FeedBackChatRepository feedBackChatRepository;
    @Autowired
    FeedBackMapper feedBackMapper;
    @Autowired
    ObjectMapper objectMapper;

    private List<Object> feedbacks = new ArrayList<>();
    private Gson gson = new Gson();
    @BeforeEach
    public void setUp() {
        // mockito를 사용하려 했으나 mockbean 설정이 잘 안 되어 포기.
        MockitoAnnotations.initMocks(this);
    }
    @AfterEach
    public void deleteDBData() {
        feedBackChatRepository.deleteAll();
    }
    @Test
    @Order(1)
    void saveFeedbackMessage() throws JsonProcessingException {
        // Given
        Long studyHistoryId = 1l;
        ChatDTO.SaveDTO saveDTO = new ChatDTO.SaveDTO();
        saveDTO.setStudyHistoryId(studyHistoryId);
        for (int i=1;i<18;i++){
            ChatDTO.FeedBackDTO feedback = new ChatDTO.FeedBackDTO();
            feedback.setMessage("test " + i); feedback.setStudyHistoryId(Long.valueOf(i));
            feedback.setReceivedUserId(2l); feedback.setSendUserId(1l);
            feedback.setCreatedAt(StringUtils.getTimeStamp(LocalDateTime.of(2020,i % 12 + 1, i,i,i,0)));
            feedbacks.add(feedback);
            redisTemplate.opsForList().rightPush(studyHistoryId.toString(), gson.toJson(feedback));
        }
        Iterable<String> lists = feedbacks.stream()
                .map(f -> gson.toJson(feedBackMapper.toEntity((ChatDTO.FeedBackDTO) f)))
                .collect(Collectors.toList());


        // when
        chatService.saveFeedbackMessage(saveDTO);

        // then
        int page = 0;
        // pagination 세팅
        Pageable pageRequest = PageRequest.of(page, 10);
        var getList = feedBackChatRepository.findAllByStudyHistoryId(pageRequest, studyHistoryId);
        // 원본 데이터의 첫번째 값.
        var data = objectMapper.readValue(lists.iterator().next(), FeedBackChat.class);
        assertEquals(data.getMessage(), getList.get(0).getMessage());
    }

//    @Test
//    @Order(2)
//    public void getFeedbackMessage() {
//        // given
//
//        Long studyHistoryId = 1l;
//        Long targetUserId = 1l;
//        Integer idx = 0;
//        var studyRoom = StudyHistory.builder().studyRoom(1l).user().build();
//        studyRoom.
//        given(studyHistoryRepository.findById(any())).willReturn(
//                studyRoom);
//
//        ChatDTO.SaveDTO saveDTO = new ChatDTO.SaveDTO();
//        saveDTO.setStudyHistoryId(studyHistoryId);
//        for (int i=1;i<21;i++){
//            ChatDTO.FeedBackDTO feedback = new ChatDTO.FeedBackDTO();
//            feedback.setMessage("test " + i); feedback.setStudyHistoryId(studyHistoryId);
//            feedback.setWrittenUserId(2l); feedback.setTargetUserId(targetUserId);
//            feedback.setCreatedAt(StringUtils.getTimeStamp(LocalDateTime.of(2020,i % 12 + 1, i,i,i,0)));
//            feedbacks.add(feedback);
//            redisTemplate.opsForList().rightPush(studyHistoryId.toString(), gson.toJson(feedback));
//        }
//        Iterable<String> lists = feedbacks.stream()
//                .map(f -> gson.toJson(feedBackMapper.toEntity((ChatDTO.FeedBackDTO) f)))
//                .collect(Collectors.toList());
//        chatService.saveFeedbackMessage(saveDTO);
//
//        // when
//        var result = chatService.getFeedbackMessage(studyHistoryId, targetUserId, idx);
//
//        // then
//        assertNotNull(result);
//        for (int i=0; i < result.size(); i++) {
//            FeedBackChat expected = (FeedBackChat) feedbacks.get(i);
//            var actual = result.get(i);
//            assertEquals(expected.getMessage(), actual.getMessage());
//        }
//    }
}