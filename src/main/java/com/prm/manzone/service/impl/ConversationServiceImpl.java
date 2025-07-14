package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Conversation;
import com.prm.manzone.entities.User;
import com.prm.manzone.mapper.ConversationMapper;
import com.prm.manzone.payload.chat.ConversationResponse;
import com.prm.manzone.payload.chat.CreateConversationRequest;
import com.prm.manzone.repository.ConversationRepository;
import com.prm.manzone.repository.UserRepository;
import com.prm.manzone.service.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements IConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;

    @Override
    public Conversation createConversation(CreateConversationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Conversation conversation = Conversation.builder()
                .user(user)
                .build();
        return conversationRepository.save(conversation);
    }

    @Override
    public Page<ConversationResponse> getAllConversations(Pageable pageable) {
        return conversationRepository.findAll(pageable).map(conversationMapper::toConversationResponse);
    }
}

