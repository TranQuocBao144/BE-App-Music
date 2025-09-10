package com.example.chat_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * DTO for transferring chat message data between client and server
 * Used for both sending and receiving messages via WebSocket
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDto {

    Long id;
    String chatId;
    Integer senderId;
    Integer recipientId;
    String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;

    String status;

    // Additional fields for UI display
    String senderUsername;
    String recipientUsername;
}
