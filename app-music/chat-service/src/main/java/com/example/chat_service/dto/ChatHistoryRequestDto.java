package com.example.chat_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for requesting chat history between two users
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatHistoryRequestDto {

    Integer senderId;
    Integer recipientId;
    Integer page;
    Integer size;
}
