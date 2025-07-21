package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Conversation;
import com.prm.manzone.entities.Message;
import com.prm.manzone.entities.User;
import com.prm.manzone.enums.MessageType;
import com.prm.manzone.payload.chat.ChatMessage;
import com.prm.manzone.repository.ConversationRepository;
import com.prm.manzone.repository.MessageRepository;
import com.prm.manzone.repository.UserRepository;
import com.prm.manzone.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {
    private final ConversationRepository conversationRepo;
    private final UserRepository userRepo;
    private final MessageRepository messageRepo;
    @Override
    public ChatMessage handleIncomingMessage(ChatMessage message) {
        Conversation conversation = conversationRepo.findById(message.getConversationId())
            .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepo.findById(message.getSenderId())
            .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message entity = new Message();
        entity.setConversation(conversation);
        entity.setSender(sender);
        entity.setType(MessageType.valueOf(message.getType().name()));
        entity.setMessage(message.getMessageText());
        entity.setImageUrl(message.getImageUrl());
        entity.setType(message.getType());

        messageRepo.save(entity);
        message.setSenderEmail(sender.getEmail());
        return message;
    }
}
