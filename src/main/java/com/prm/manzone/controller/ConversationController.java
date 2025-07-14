package com.prm.manzone.controller;

import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.payload.chat.ConversationResponse;
import com.prm.manzone.service.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final IConversationService conversationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ConversationResponse>>> getAllConversations(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ConversationResponse> conversations = conversationService.getAllConversations(pageable);
        ApiResponse<Page<ConversationResponse>> response = ApiResponse.<Page<ConversationResponse>>builder()
                .success(true)
                .data(conversations)
                .build();
        return ResponseEntity.ok(response);
    }

}
