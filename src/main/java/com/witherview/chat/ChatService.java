package com.witherview.chat;

import com.witherview.database.entity.StudyHistory;
import com.witherview.database.entity.StudyRoomParticipant;
import com.witherview.database.repository.*;
import com.witherview.groupPractice.exception.NotFoundStudyHistory;
import com.witherview.groupPractice.exception.NotFoundStudyRoom;
import com.witherview.groupPractice.exception.NotJoinedStudyRoom;
import com.witherview.groupPractice.exception.NotOwnedStudyHistory;
import com.witherview.utils.ChatMessageMapper;
import com.witherview.utils.FeedBackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return content.stream().map(feedbackEntity -> feedBackMapper.toDto(feedbackEntity)).collect(Collectors.toList());
    }

    public List<ChatDTO.MessageDTO> getChatMessageByStudyRoomId(Long userId, Long studyRoomId, Integer idx) {
        studyRoomRepository.findById(studyRoomId).orElseThrow(NotFoundStudyRoom::new);
        StudyRoomParticipant studyRoomParticipant = studyRoomParticipantRepository.findByStudyRoomIdAndUserId(studyRoomId, userId);

        if(studyRoomParticipant == null) throw new NotJoinedStudyRoom();

        int page = idx == null ? 0 : idx;
        Pageable pageRequest = PageRequest.of(page, 20,
                Sort.by("timestamp").descending()
        );
        var result = chatRepository.findAllByStudyRoomId(studyRoomId, pageRequest);
        var content = result.getContent();
        return content.stream().map(chatEntity -> chatMessageMapper.toDto(chatEntity)).collect(Collectors.toList());
    }

    public StudyHistory findStudyHistory(Long id) {
        return studyHistoryRepository.findById(id).orElseThrow(NotFoundStudyHistory::new);
    }
}
