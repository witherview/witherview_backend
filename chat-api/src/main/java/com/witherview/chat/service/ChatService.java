package com.witherview.chat.service;

import com.witherview.chat.configuration.JWSAuthenticationToken;
import com.witherview.chat.dto.ChatDTO.FeedBackDTO;
import com.witherview.chat.dto.ChatDTO.MessageDTO;
import com.witherview.chat.mapper.ChatMessageMapper;
import com.witherview.chat.mapper.FeedBackMapper;
import entity.StudyHistory;
import entity.StudyRoom;
import exception.ErrorCode;
import exception.account.InvalidJwtTokenException;
import exception.study.NotFoundStudyHistory;
import exception.study.NotFoundStudyRoom;
import exception.study.NotJoinedStudyRoom;
import exception.study.NotOwnedStudyHistory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import mongo.repository.ChatRepository;
import mongo.repository.FeedBackChatRepository;
import repository.StudyHistoryRepository;
import repository.StudyRoomParticipantRepository;
import repository.StudyRoomRepository;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final FeedBackMapper feedBackMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final StudyHistoryRepository studyHistoryRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomParticipantRepository studyRoomParticipantRepository;
    private final FeedBackChatRepository feedBackChatRepository;
    private final ChatRepository chatRepository;
    @Qualifier("websocket")
    private final AuthenticationManager authenticationManager;

    public List<FeedBackDTO> getFeedbackMessageByReceivedUserId(String userId, Long historyId, Integer idx) {
        StudyHistory studyHistory = findStudyHistory(historyId);
        if(!studyHistory.getUser().getId().equals(userId)) throw new NotOwnedStudyHistory();

        int page = idx == null ? 0 : idx;
        Pageable pageRequest = PageRequest.of(page, 10,
                Sort.by("studyHistoryId").ascending()
        );
        var result = feedBackChatRepository.findAllByReceivedUserIdAndStudyHistoryId(userId, historyId, pageRequest);
        var content = result.getContent();
        return content.stream().map(feedBackMapper::toDto).collect(Collectors.toList());
    }

    public List<FeedBackDTO> getFeedbackMessageByReceivedUserIdAndSendUserId(String receivedUserId, Long historyId, String sendUserId, Integer idx) {
        StudyHistory studyHistory = findStudyHistory(historyId);
        if(!studyHistory.getUser().getId().equals(receivedUserId)) throw new NotOwnedStudyHistory();

        int page = idx == null ? 0 : idx;
        Pageable pageRequest = PageRequest.of(page, 10,
                Sort.by("studyHistoryId").ascending()
        );
        var result = feedBackChatRepository.findAllByReceivedUserIdAndStudyHistoryIdAndSendUserId(receivedUserId, historyId, sendUserId, pageRequest);
        var content = result.getContent();
        return content.stream().map(feedBackMapper::toDto).collect(Collectors.toList());
    }

    public List<MessageDTO> getChatMessageByStudyRoomId(String userId, Long studyRoomId, Integer idx) {
        studyRoomRepository.findById(studyRoomId).orElseThrow(NotFoundStudyRoom::new);
        var studyRoomParticipant = studyRoomParticipantRepository.findByStudyRoomIdAndUserId(studyRoomId, userId)
                .orElseThrow(NotJoinedStudyRoom::new);

        int page = idx == null ? 0 : idx;
        Pageable pageRequest = PageRequest.of(page, 20,
                Sort.by("timestamp").descending()
        );
        var result = chatRepository.findAllByStudyRoomId(studyRoomId, pageRequest);
        var content = result.getContent();
        return content.stream().map(chatMessageMapper::toDto).collect(Collectors.toList());
    }

    public StudyHistory findStudyHistory(Long id) {
        return studyHistoryRepository.findById(id).orElseThrow(NotFoundStudyHistory::new);
    }

    public StudyRoom findStudyRoom(Long id) {
        return studyRoomRepository.findById(id).orElseThrow(NotFoundStudyRoom::new);
    }

    public String getUserIdFromTokenString(String tokenString) {
        tokenString = tokenString.replace("Bearer " , "");
        if (tokenString.isEmpty()) {
            throw new InvalidJwtTokenException(ErrorCode.INVALID_JWT_TOKEN);
        }
        JWSAuthenticationToken token = new JWSAuthenticationToken(tokenString);
        var auth = authenticationManager.authenticate(token);
        String userId = (String) auth.getCredentials();
        return userId;
    }
}
