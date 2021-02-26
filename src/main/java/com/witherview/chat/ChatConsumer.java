package com.witherview.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witherview.database.entity.*;
import com.witherview.database.repository.ChatRepository;
import com.witherview.database.repository.FeedBackChatRepository;
import com.witherview.database.repository.StudyHistoryRepository;
import com.witherview.database.repository.StudyRoomRepository;
import com.witherview.database.repository.StudyRoomParticipantRepository;
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
    public void consumeChatMessage(String message) throws JsonProcessingException {
        ChatDTO.MessageDTO messageDTO = objectMapper.readValue(message, ChatDTO.MessageDTO.class);
        Chat chat = objectMapper.readValue(message, Chat.class);
        Optional<StudyRoom> studyRoom = studyRoomRepository.findById(messageDTO.getStudyRoomId());
        StudyRoomParticipant studyRoomParticipant = studyRoomParticipantRepository
                .findByStudyRoomIdAndUserId(messageDTO.getStudyRoomId(), messageDTO.getUserId());

        if(studyRoom.isEmpty() || studyRoomParticipant == null) return;
        chatRepository.insert(chat);
        sendChat(message);
    }

    @Transactional
    @RabbitListener(queues = "feedback-queue")
    public void consumeFeedbackMessage(String message) throws JsonProcessingException {
        ChatDTO.FeedBackDTO feedBackDTO = objectMapper.readValue(message, ChatDTO.FeedBackDTO.class);
        FeedBackChat feedBackChat = objectMapper.readValue(message, FeedBackChat.class);
        Optional<StudyHistory> studyHistory = studyHistoryRepository.findById(feedBackDTO.getStudyHistoryId());

        if(studyHistory.isEmpty() || studyHistory.get().getUser().getId() != feedBackDTO.getReceivedUserId()) return;

        feedBackChatRepository.insert(feedBackChat);
        sendFeedBack(message);
    }

    private void sendChat(String message) {
        redisTemplate.convertAndSend(studyRoomChannelTopic.getTopic(), message);
    }

    private void sendFeedBack(String message) {
        redisTemplate.convertAndSend(feedBackChannelTopic.getTopic(), message);
    }
}
