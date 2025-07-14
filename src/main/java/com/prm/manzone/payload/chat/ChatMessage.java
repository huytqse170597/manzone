package com.prm.manzone.payload.chat;

import com.prm.manzone.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private int conversationId;
    private int senderId;
    private String messageText;
    private String imageUrl;
    private MessageType type;
}
