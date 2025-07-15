package com.prm.manzone.payload.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private int id;
    private Integer userId;
    private String email;
    private String title;
    private Instant createdAt;
    private Instant updatedAt;
}
