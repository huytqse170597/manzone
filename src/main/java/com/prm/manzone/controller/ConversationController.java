package com.prm.manzone.controller;

import com.prm.manzone.entities.Conversation;
import com.prm.manzone.mapper.ConversationMapper;
import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.payload.chat.ConversationResponse;
import com.prm.manzone.payload.chat.CreateConversationRequest;
import com.prm.manzone.payload.chat.UpdateConversationRequest;
import com.prm.manzone.service.IConversationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ConversationController {

    private final IConversationService conversationService;
    private final ConversationMapper conversationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ConversationResponse>>> getAllConversations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort, "createdAt"));
        Page<ConversationResponse> conversations = conversationService.getAllConversations(pageable);
        ApiResponse<Page<ConversationResponse>> response = ApiResponse.<Page<ConversationResponse>>builder()
                .success(true)
                .data(conversations)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConversationResponse>> createConversation(@RequestBody CreateConversationRequest request) {
        Conversation conversation = conversationService.createConversation(request);
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);
        ApiResponse<ConversationResponse> response = ApiResponse.<ConversationResponse>builder()
                .success(true)
                .data(conversationResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ConversationResponse>> updateConversation(@PathVariable Integer id, @RequestBody UpdateConversationRequest request) {
        Conversation conversation = conversationService.updateConversation(id, request);
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);
        ApiResponse<ConversationResponse> response = ApiResponse.<ConversationResponse>builder()
                .success(true)
                .data(conversationResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getConversationByUserId(@PathVariable Integer userId) {
        List<Conversation> conversations = conversationService.getConversationByUserId(userId);
        List<ConversationResponse> conversationResponses = conversations.stream().map(conversationMapper::toConversationResponse).toList();
        ApiResponse<List<ConversationResponse>> response = ApiResponse.<List<ConversationResponse>>builder()
                .success(true)
                .data(conversationResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/markdone/{conversationId}")
    public ResponseEntity<ApiResponse<Void>> markConversationAsDone(@PathVariable Integer conversationId) {
        conversationService.markConversationAsDone(conversationId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Conversation marked as done successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
