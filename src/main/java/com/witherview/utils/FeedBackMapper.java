package com.witherview.utils;

import com.witherview.chat.ChatDTO;
import com.witherview.database.entity.FeedBackChat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedBackMapper extends EntityMapper<ChatDTO.FeedBackDTO, FeedBackChat> {

}
