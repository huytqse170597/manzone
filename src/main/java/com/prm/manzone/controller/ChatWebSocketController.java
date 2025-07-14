package com.prm.manzone.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.prm.manzone.payload.chat.ChatMessage;
import com.prm.manzone.service.IChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final IChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        ChatMessage saved = chatService.handleIncomingMessage(message);

        // Gửi đến đúng channel
        messagingTemplate.convertAndSend(
            "/topic/conversation." + saved.getConversationId(), saved
        );
    }
}
