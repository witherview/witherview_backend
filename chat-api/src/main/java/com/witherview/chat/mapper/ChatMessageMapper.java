package com.witherview.chat.mapper;

import com.witherview.chat.dto.ChatDTO.MessageDTO;
import com.witherview.mongo.entity.Chat;
import org.mapstruct.Mapper;
import util.EntityMapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends EntityMapper<MessageDTO, Chat> {
}
