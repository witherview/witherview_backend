package com.witherview.utils;

import com.witherview.chat.ChatDTO;
import com.witherview.database.entity.Chat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends EntityMapper<ChatDTO.MessageDTO, Chat> {
}
