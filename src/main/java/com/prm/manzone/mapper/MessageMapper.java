package com.prm.manzone.mapper;

import com.prm.manzone.entities.Message;
import com.prm.manzone.payload.chat.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.email", target = "senderEmail")
    MessageResponse toMessageResponse(Message message);
}
