package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Conversation;
import com.prm.manzone.entities.User;
import com.prm.manzone.mapper.ConversationMapper;
import com.prm.manzone.payload.chat.ConversationResponse;
import com.prm.manzone.payload.chat.CreateConversationRequest;
import com.prm.manzone.payload.chat.UpdateConversationRequest;
import com.prm.manzone.repository.ConversationRepository;
import com.prm.manzone.repository.UserRepository;
import com.prm.manzone.service.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements IConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public Conversation createConversation(CreateConversationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Conversation conversation = Conversation.builder()
                .user(user)
                .title(null != request.getTitle() ? request.getTitle() : "New Conversation")
                .done(false)
                .build();
        simpMessagingTemplate.convertAndSend("/topic/new-conversation", conversation);
        return conversationRepository.save(conversation);
    }

    @Override
    public Page<ConversationResponse> getAllConversations(Pageable pageable) {
        return conversationRepository.findAll(pageable).map(conversationMapper::toConversationResponse);
    }

    @Override
    public Conversation updateConversation(Integer id, UpdateConversationRequest request) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (request.getTitle() != null) {
            conversation.setTitle(request.getTitle());
        }
        if (request.getStatus() != null) {
            conversation.setStatus(request.getStatus());
        }

        return conversationRepository.save(conversation);
    }

    @Override
    public List<Conversation> getConversationByUserId(Integer userId) {
        return conversationRepository.findByUserId(userId);
    }

    @Override
    public void markConversationAsDone(Integer conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        conversation.setDone(true);
        simpMessagingTemplate.convertAndSend("/topic/conversation-done", conversationMapper.toConversationResponse(conversation));
        conversationRepository.save(conversation);
    }
}
