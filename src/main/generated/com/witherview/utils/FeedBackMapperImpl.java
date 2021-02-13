package com.witherview.utils;

import com.witherview.chat.ChatDTO.FeedBackDTO;
import com.witherview.database.entity.FeedBackChat;
import com.witherview.database.entity.FeedBackChat.FeedBackChatBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-13T17:18:05+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.10 (Azul Systems, Inc.)"
)
@Component
public class FeedBackMapperImpl implements FeedBackMapper {

    @Override
    public FeedBackChat toEntity(FeedBackDTO arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FeedBackChatBuilder feedBackChat = FeedBackChat.builder();

        feedBackChat.id( arg0.getId() );
        feedBackChat.targetUserId( arg0.getTargetUserId() );
        feedBackChat.writtenUserId( arg0.getWrittenUserId() );
        feedBackChat.studyHistoryId( arg0.getStudyHistoryId() );
        feedBackChat.message( arg0.getMessage() );
        feedBackChat.createdAt( arg0.getCreatedAt() );
        feedBackChat.timestamp( arg0.getTimestamp() );

        return feedBackChat.build();
    }

    @Override
    public FeedBackDTO toDto(FeedBackChat arg0) {
        if ( arg0 == null ) {
            return null;
        }

        FeedBackDTO feedBackDTO = new FeedBackDTO();

        feedBackDTO.setId( arg0.getId() );
        feedBackDTO.setStudyHistoryId( arg0.getStudyHistoryId() );
        feedBackDTO.setWrittenUserId( arg0.getWrittenUserId() );
        feedBackDTO.setTargetUserId( arg0.getTargetUserId() );
        feedBackDTO.setMessage( arg0.getMessage() );
        feedBackDTO.setCreatedAt( arg0.getCreatedAt() );
        feedBackDTO.setTimestamp( arg0.getTimestamp() );

        return feedBackDTO;
    }
}
