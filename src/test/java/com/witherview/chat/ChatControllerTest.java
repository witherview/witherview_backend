package com.witherview.chat;


import com.witherview.database.entity.StudyRoom;
import com.witherview.groupPractice.GroupStudy.GroupStudyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
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
class ChatControllerTest {
    @MockBean
    private GroupStudyService groupStudyService;
    @LocalServerPort
    private Integer port;
    private WebSocketStompClient webSocketStompClient;
    private BlockingQueue<ChatDTO.MessageDTO> blockingQueue;

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
                .get(10, SECONDS);

        // 채팅메시지 생성
        ChatDTO.MessageDTO msg = new ChatDTO.MessageDTO();
        msg.setRoomId(1l); msg.setSender("test");
        msg.setType("1"); msg.setContents("testMessage");

        // 해당 방 구독
        session.subscribe("/topic/room." + msg.getRoomId(), new CustomStompFrameHandler());
        // 메시지 전송
        session.send("/pub/chat", msg);
        assertEquals(msg.getContents(), blockingQueue.poll(10, SECONDS).getContents());
    }

    @Test
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

        // When
        // 해당 방 구독 -> 방이 문자열인 경우
        session.subscribe("/topic/room." + msg.getSender(), new CustomStompFrameHandler());
        // 메시지 전송
        session.send("/pub/chat", msg);

        // Then
        assertEquals(msg.getContents(), blockingQueue.poll(10, SECONDS).getContents());
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
            System.out.println("Receive Message : " + payload.toString());
            // 메시지 저장
            blockingQueue.add((ChatDTO.MessageDTO) payload);
        }
    }
}