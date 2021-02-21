package com.witherview.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witherview.database.entity.Chat;
import com.witherview.database.entity.FeedBackChat;
import com.witherview.database.entity.StudyHistory;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.repository.ChatRepository;
import com.witherview.database.repository.FeedBackChatRepository;
import com.witherview.database.repository.StudyHistoryRepository;
import com.witherview.database.repository.StudyRoomRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ChatConsumer {
    private final SimpMessageSendingOperations messagingTemplate;
    private final FeedBackChatRepository feedBackChatRepository;
    private final ChatRepository chatRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatConsumer(SimpMessageSendingOperations messagingTemplate,
                        FeedBackChatRepository feedBackChatRepository,
                        ChatRepository chatRepository,
                        StudyRoomRepository studyRoomRepository,
                        StudyHistoryRepository studyHistoryRepository,
                        ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.feedBackChatRepository = feedBackChatRepository;
        this.chatRepository = chatRepository;
        this.studyRoomRepository = studyRoomRepository;
        this.studyHistoryRepository = studyHistoryRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @RabbitListener(queues = "chat-queue")
    public void consumeChatMessage(String message) throws JsonProcessingException {
        ChatDTO.MessageDTO messageDTO = objectMapper.readValue(message, ChatDTO.MessageDTO.class);
        Chat chat = objectMapper.readValue(message, Chat.class);
        Optional<StudyRoom> studyRoom = studyRoomRepository.findById(messageDTO.getStudyRoomId());

        if(studyRoom.isEmpty()) return;
        chatRepository.insert(chat);
        sendChat(message, messageDTO.getStudyRoomId());
    }

    @Transactional
    @RabbitListener(queues = "feedback-queue")
    public void consumeFeedbackMessage(String message) throws JsonProcessingException {
        ChatDTO.FeedBackDTO feedBackDTO = objectMapper.readValue(message, ChatDTO.FeedBackDTO.class);
        FeedBackChat feedBackChat = objectMapper.readValue(message, FeedBackChat.class);
        Optional<StudyHistory> studyHistory = studyHistoryRepository.findById(feedBackDTO.getStudyHistoryId());

        if(studyHistory.isEmpty() || studyHistory.get().getUser().getId() != feedBackDTO.getReceivedUserId()) return;

        feedBackChatRepository.insert(feedBackChat);
        sendFeedBack(message, feedBackDTO.getStudyHistoryId());
    }

    private void sendChat(String message, Long roomId) {
        messagingTemplate.convertAndSend("/sub/room." + roomId, message);
    }

    private void sendFeedBack(String message, Long historyId) {
        messagingTemplate.convertAndSend("/sub/feedback." + historyId, message);
    }
}
