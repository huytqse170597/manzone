package com.prm.manzone.controller;

import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.payload.chat.MessageResponse;
import com.prm.manzone.service.IMessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    @GetMapping("/api/conversations/{conversationId}/messages")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getMessagesByConversationId(
            @PathVariable Integer conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort, "createdAt"));
        Page<MessageResponse> messages = messageService.getMessagesByConversationId(conversationId, pageable);
        ApiResponse<Page<MessageResponse>> response = ApiResponse.<Page<MessageResponse>>builder()
                .success(true)
                .data(messages)
                .build();
        return ResponseEntity.ok(response);
    }
}
