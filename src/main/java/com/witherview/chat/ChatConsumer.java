package com.witherview.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witherview.database.entity.*;
import com.witherview.database.repository.ChatRepository;
import com.witherview.database.repository.FeedBackChatRepository;
import com.witherview.database.repository.StudyHistoryRepository;
import com.witherview.database.repository.StudyRoomRepository;
import com.witherview.database.repository.StudyRoomParticipantRepository;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.groupPractice.exception.NotJoinedStudyRoom;
import com.witherview.groupPractice.exception.NotOwnedStudyHistory;
import com.witherview.selfPractice.exception.NotFoundHistory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ChatConsumer {
    private final FeedBackChatRepository feedBackChatRepository;
    private final ChatRepository chatRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyRoomParticipantRepository studyRoomParticipantRepository;
    private final ObjectMapper objectMapper;
    // redis pub/sub
    private final RedisTemplate redisTemplate;
    private final ChannelTopic studyRoomChannelTopic;
    private final ChannelTopic feedBackChannelTopic;

    @Autowired
    public ChatConsumer(FeedBackChatRepository feedBackChatRepository,
                        ChatRepository chatRepository,
                        StudyRoomRepository studyRoomRepository,
                        StudyHistoryRepository studyHistoryRepository,
                        StudyRoomParticipantRepository studyRoomParticipantRepository,
                        ObjectMapper objectMapper,
                        RedisTemplate redisTemplate,
                        ChannelTopic studyRoomChannelTopic,
                        ChannelTopic feedBackChannelTopic) {
        this.feedBackChatRepository = feedBackChatRepository;
        this.chatRepository = chatRepository;
        this.studyRoomRepository = studyRoomRepository;
        this.studyHistoryRepository = studyHistoryRepository;
        this.studyRoomParticipantRepository = studyRoomParticipantRepository;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.studyRoomChannelTopic = studyRoomChannelTopic;
        this.feedBackChannelTopic = feedBackChannelTopic;
    }

    @Transactional
    @RabbitListener(queues = "chat-queue")
    public void consumeChatMessage(String message) {
        try {
            ChatDTO.MessageDTO messageDTO = objectMapper.readValue(message, ChatDTO.MessageDTO.class);
            Chat chat = objectMapper.readValue(message, Chat.class);
            studyRoomRepository.findById(messageDTO.getStudyRoomId())
                                .orElseThrow(NotFoundStudyRoom::new);
            studyRoomParticipantRepository.findByStudyRoomIdAndUserId(messageDTO.getStudyRoomId(), messageDTO.getUserId())
                                .orElseThrow(NotJoinedStudyRoom::new);

            chatRepository.insert(chat);
            sendChat(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Transactional
    @RabbitListener(queues = "feedback-queue")
    public void consumeFeedbackMessage(String message) {
        try {
            ChatDTO.FeedBackDTO feedBackDTO = objectMapper.readValue(message, ChatDTO.FeedBackDTO.class);
            FeedBackChat feedBackChat = objectMapper.readValue(message, FeedBackChat.class);
            StudyHistory studyHistory = studyHistoryRepository.findById(feedBackDTO.getStudyHistoryId())
                                    .orElseThrow(NotFoundHistory::new);

            if(studyHistory.getUser().getId() != feedBackDTO.getReceivedUserId()) throw new NotOwnedStudyHistory();

            feedBackChatRepository.insert(feedBackChat);
            sendFeedBack(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void sendChat(String message) {
        redisTemplate.convertAndSend(studyRoomChannelTopic.getTopic(), message);
    }

    private void sendFeedBack(String message) {
        redisTemplate.convertAndSend(feedBackChannelTopic.getTopic(), message);
    }
}
