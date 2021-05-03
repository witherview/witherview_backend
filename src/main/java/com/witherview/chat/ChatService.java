package com.witherview.chat;

import com.witherview.account.exception.InvalidJwtTokenException;
import com.witherview.configuration.authentication.JWSAuthenticationToken;
import com.witherview.database.entity.StudyHistory;
import com.witherview.database.entity.StudyRoomParticipant;
import com.witherview.database.repository.*;
import com.witherview.exception.ErrorCode;
import com.witherview.groupPractice.exception.NotFoundStudyHistory;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.groupPractice.exception.NotJoinedStudyRoom;
import com.witherview.groupPractice.exception.NotOwnedStudyHistory;
import com.witherview.utils.ChatMessageMapper;
import com.witherview.utils.FeedBackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final FeedBackMapper feedBackMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final FeedBackChatRepository feedBackChatRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final ChatRepository chatRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRoomParticipantRepository studyRoomParticipantRepository;
    @Qualifier("websocket")
    private final AuthenticationManager authenticationManager;

    public List<ChatDTO.FeedBackDTO> getFeedbackMessageByReceivedUserId(String userId, Long historyId, Integer idx) {
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

    public List<ChatDTO.FeedBackDTO> getFeedbackMessageByReceivedUserIdAndSendUserId(String receivedUserId, Long historyId, String sendUserId, Integer idx) {
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

    public List<ChatDTO.MessageDTO> getChatMessageByStudyRoomId(String userId, Long studyRoomId, Integer idx) {
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
