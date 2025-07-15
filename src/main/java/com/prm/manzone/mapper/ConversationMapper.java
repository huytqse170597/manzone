package com.prm.manzone.mapper;

import com.prm.manzone.entities.Conversation;
import com.prm.manzone.payload.chat.ConversationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    ConversationResponse toConversationResponse(Conversation conversation);
}
