package com.witherview.chat.rabbitMQ;

import com.witherview.chat.dto.ChatDTO.FeedBackDTO;
import com.witherview.chat.dto.ChatDTO.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.StudyHistory;
import exception.study.NotFoundHistory;
import exception.study.NotFoundStudyRoom;
import exception.study.NotJoinedStudyRoom;
import exception.study.NotOwnedStudyHistory;
import lombok.RequiredArgsConstructor;
import mongo.entity.Chat;
import mongo.entity.FeedBackChat;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import mongo.repository.ChatRepository;
import mongo.repository.FeedBackChatRepository;
import repository.StudyHistoryRepository;
import repository.StudyRoomParticipantRepository;
import repository.StudyRoomRepository;

@Component
@RequiredArgsConstructor
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

    @Transactional
    @RabbitListener(queues = "chat-queue")
    public void consumeChatMessage(String message) {
        try {
            MessageDTO messageDTO = objectMapper.readValue(message, MessageDTO.class);
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
            FeedBackDTO feedBackDTO = objectMapper.readValue(message, FeedBackDTO.class);
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
