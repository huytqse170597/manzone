package com.prm.manzone.payload.chat;

import com.prm.manzone.enums.ConversationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConversationRequest {
    private String title;
    private ConversationStatus status;
}
