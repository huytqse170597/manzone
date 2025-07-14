package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Message;
import com.prm.manzone.mapper.MessageMapper;
import com.prm.manzone.payload.chat.MessageResponse;
import com.prm.manzone.repository.MessageRepository;
import com.prm.manzone.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public Page<MessageResponse> getMessagesByConversationId(Integer conversationId, Pageable pageable) {
        Page<Message> messages = messageRepository.findByConversationId(conversationId, pageable);
        return messages.map(messageMapper::toMessageResponse);
    }
}
