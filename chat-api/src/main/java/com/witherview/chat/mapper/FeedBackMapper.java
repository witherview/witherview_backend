package com.witherview.chat.mapper;

import com.witherview.chat.dto.ChatDTO.FeedBackDTO;
import com.witherview.mongo.entity.FeedBackChat;
import org.mapstruct.Mapper;
import util.EntityMapper;

@Mapper(componentModel = "spring")
public interface FeedBackMapper extends EntityMapper<FeedBackDTO, FeedBackChat> {

}
