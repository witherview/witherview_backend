package com.witherview.chat;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witherview.database.entity.StudyHistory;
import com.witherview.database.entity.StudyRoom;
import com.witherview.groupPractice.GroupStudy.GroupStudyService;
import com.witherview.groupPractice.history.StudyHistoryService;
import com.witherview.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChatControllerTest {
    @MockBean
    private GroupStudyService groupStudyService;
    @MockBean
    private StudyHistoryService studyHistoryService;
    @LocalServerPort
    private Integer port;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private WebSocketStompClient webSocketStompClient;
    private BlockingQueue<Object> blockingQueue;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // 메시지 저장할 queue. blocking Queue임에 유의
        blockingQueue = new ArrayBlockingQueue(10);
        this.webSocketStompClient =
                new WebSocketStompClient(new SockJsClient(
                        Arrays.asList(new WebSocketTransport(
                                new StandardWebSocketClient())
                        )
                ));
    }

    private String getWsPath() {
        return String.format("ws://localhost:%d/socket", port);
    }

    @Test
    void messageSuccess() throws InterruptedException, ExecutionException, TimeoutException {
        // message broker의 presend 해결
        given(groupStudyService.findRoom(any())).willReturn(StudyRoom.builder().build());
        // Json Converter
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        // 세션 생성, timeout 시간 설정
        StompSession session = webSocketStompClient.connect(getWsPath(), new CustomStompFrameHandler() {
        })
                .get(5, SECONDS);

        // 채팅메시지 생성
        ChatDTO.MessageDTO msg = new ChatDTO.MessageDTO();
        msg.setRoomId(1l); msg.setSender("test");
        msg.setType("1"); msg.setContents("testMessage");
        msg.setCreatedAt(StringUtils.getTimeStamp(LocalDateTime.now()));
        // 해당 방 구독
        session.subscribe("/topic/room." + msg.getRoomId(), new CustomStompFrameHandler());
        // 메시지 전송
        session.send("/pub/chat.room", msg);
        ChatDTO.MessageDTO result = (ChatDTO.MessageDTO) blockingQueue.poll(10, SECONDS);
        assertEquals(msg.getContents(), result.getContents());
    }

    @Test
    @Disabled
    void messageWithInvalidUrl() throws InterruptedException, ExecutionException, TimeoutException {
        //// Given

        // message broker의 presend 해결
        given(groupStudyService.findRoom(any())).willReturn(StudyRoom.builder().build());
        // Json Converter
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        // 세션 생성, timeout 시간 설정
        StompSession session = webSocketStompClient.connect(getWsPath(), new CustomStompFrameHandler() {
        })
                .get(10, SECONDS);

        // 채팅메시지 생성
        ChatDTO.MessageDTO msg = new ChatDTO.MessageDTO();
        msg.setRoomId(1l); msg.setSender("test");
        msg.setType("1"); msg.setContents("testMessage");
        msg.setCreatedAt(StringUtils.getTimeStamp(LocalDateTime.now()));

        // When
        // 해당 방 구독 -> 방이 문자열인 경우
        session.subscribe("/topic/room." + msg.getSender(), new CustomStompFrameHandler());
        // 메시지 전송
        session.send("/pub/chat.room", msg);

        // Then
        var result = (ChatDTO.MessageDTO) blockingQueue.poll(10, SECONDS);
        assertEquals(msg.getContents(), result.getContents());
    }

    @Test
    public void feedback() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {

        // Given
        given(studyHistoryService.findStudyHistory(any())).willReturn(StudyHistory.builder().build());
        Long studyHistoryId = 1l;

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession session = webSocketStompClient.connect(getWsPath(), new CustomStompFrameHandler() {
        })
                .get(5, SECONDS);

        // When
        var feedback = new ChatDTO.FeedBackDTO();
        feedback.setMessage("test"); feedback.setStudyHistoryId(studyHistoryId);
        feedback.setReceivedUserId(2l); feedback.setSendUserId(1l);
        feedback.setCreatedAt(StringUtils.getTimeStamp(LocalDateTime.now()));

        session.subscribe("/topic/feedback." + feedback.getStudyHistoryId(), new CustomStompFrameHandler());
        session.send("/pub/chat.feedback", feedback);

        // Then
        var resultJson = (String) redisTemplate.opsForList().leftPop(studyHistoryId.toString(), 10, SECONDS);
        var result = objectMapper.readValue(resultJson, ChatDTO.FeedBackDTO.class);
        assertEquals(2, result.getReceivedUserId());
    }
    class CustomStompFrameHandler extends StompSessionHandlerAdapter implements StompFrameHandler {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("connected");
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            System.out.println("getPayloadType");
            System.out.println(headers.toString());
            return ChatDTO.MessageDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println(payload.getClass());
            System.out.println("Received Message : " + payload.toString());
            // 메시지 저장
            if (payload instanceof ChatDTO.MessageDTO)
                blockingQueue.add((ChatDTO.MessageDTO) payload);
            else if (payload instanceof ChatDTO.FeedBackDTO)
                blockingQueue.add((ChatDTO.FeedBackDTO) payload);
        }
    }
}