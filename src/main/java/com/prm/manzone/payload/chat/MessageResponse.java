package com.prm.manzone.payload.chat;

import com.prm.manzone.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private int id;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer senderId;
    private String senderEmail;
    private MessageType type;
    private String message;
    private String imageUrl;
}
