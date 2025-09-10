package com.example.chat_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for chat notifications sent to users
 * Provides lightweight notification data without full message content
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatNotificationDto {

    Long messageId;
    String chatId;
    Integer senderId;
    String senderUsername;
    String content;
    Integer unreadCount;
}
