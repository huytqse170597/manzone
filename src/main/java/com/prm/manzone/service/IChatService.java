package com.prm.manzone.service;

import com.prm.manzone.payload.chat.ChatMessage;
import org.springframework.data.domain.Page;

public interface IChatService {
    ChatMessage handleIncomingMessage(ChatMessage message);
    // You can add more methods here as needed, e.g., getMessagesByConversation, etc.
}

