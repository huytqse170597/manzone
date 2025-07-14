package com.prm.manzone.service;

import com.prm.manzone.entities.Conversation;
import com.prm.manzone.payload.chat.ConversationResponse;
import com.prm.manzone.payload.chat.CreateConversationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IConversationService {
    Conversation createConversation(CreateConversationRequest request);
    Page<ConversationResponse> getAllConversations(Pageable pageable);
}

