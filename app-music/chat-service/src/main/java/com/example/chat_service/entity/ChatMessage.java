package com.example.chat_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * ChatMessage entity for storing chat message history
 * Supports private messaging between Admin and Artist users
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Chat ID to group messages in the same conversation
     * Format: smaller_userId-larger_userId (e.g., "1-5")
     */
    @Column(nullable = false)
    String chatId;

    /**
     * ID of the user who sent the message
     */
    @Column(nullable = false)
    Integer senderId;

    /**
     * ID of the user who receives the message
     */
    @Column(nullable = false)
    Integer recipientId;

    /**
     * Content of the message
     */
    @Column(nullable = false, length = 2000)
    String content;

    /**
     * Timestamp when the message was sent
     */
    @Column(nullable = false)
    LocalDateTime timestamp;

    /**
     * Message status (sent, delivered, read)
     */
    @Builder.Default
    String status = "sent";

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
