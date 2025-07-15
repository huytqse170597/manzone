package com.prm.manzone.service;

import com.prm.manzone.payload.chat.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMessageService {
    Page<MessageResponse> getMessagesByConversationId(Integer conversationId, Pageable pageable);
}
