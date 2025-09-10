package com.example.chat_service.controller;

import com.example.chat_service.dto.ChatMessageDto;
import com.example.chat_service.dto.ChatNotificationDto;
import com.example.chat_service.entity.ChatMessage;
import com.example.chat_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket controller for handling real-time chat messages
 * Processes incoming messages and routes them to appropriate recipients
 *
 * Message Flow:
 * 1. Client sends message to /app/chat
 * 2. Controller processes and validates message
 * 3. Message is saved to database
 * 4. Message is sent to recipient via /user/{recipientId}/queue/messages
 * 5. Notification is sent to recipient via /user/{recipientId}/queue/notifications
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handle incoming chat messages from WebSocket clients
     * Route: /app/chat
     *
     * Business Logic:
     * - Validates message content and user permissions
     * - Saves message to database
     * - Sends message to recipient
     * - Sends notification to recipient
     *
     * @param chatMessage the incoming message DTO
     * @param principal the WebSocket session principal (for authentication)
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessage, Principal principal) {
        log.info("Processing message from user {} to user {}",
                chatMessage.getSenderId(), chatMessage.getRecipientId());

        try {
            // Save message using business logic in service layer
            ChatMessageDto savedMessage = chatService.saveMessage(chatMessage);

            // Send message to recipient's personal queue
            String recipientDestination = "/user/" + savedMessage.getRecipientId() + "/queue/messages";
            messagingTemplate.convertAndSend(recipientDestination, savedMessage);

            log.debug("Message sent to recipient queue: {}", recipientDestination);

            // Create and send notification to recipient
            ChatMessage messageEntity = convertToEntity(savedMessage);
            ChatNotificationDto notification = chatService.createNotification(messageEntity);

            String notificationDestination = "/user/" + savedMessage.getRecipientId() + "/queue/notifications";
            messagingTemplate.convertAndSend(notificationDestination, notification);

            log.debug("Notification sent to recipient: {}", notificationDestination);

            // Optionally, send confirmation back to sender
            String senderDestination = "/user/" + savedMessage.getSenderId() + "/queue/messages";
            messagingTemplate.convertAndSend(senderDestination, savedMessage);

            log.info("Message processed successfully: messageId={}", savedMessage.getId());

        } catch (Exception ex) {
            log.error("Error processing message from user {} to user {}: {}",
                    chatMessage.getSenderId(), chatMessage.getRecipientId(), ex.getMessage());

            // Send error back to sender
            String errorDestination = "/user/" + chatMessage.getSenderId() + "/queue/errors";
            messagingTemplate.convertAndSend(errorDestination,
                    "Failed to send message: " + ex.getMessage());
        }
    }

    /**
     * Handle typing indicator messages
     * Route: /app/typing
     *
     * @param typingIndicator the typing indicator DTO
     */
    @MessageMapping("/typing")
    public void handleTypingIndicator(@Payload TypingIndicatorDto typingIndicator) {
        log.debug("User {} is typing to user {}",
                typingIndicator.getSenderId(), typingIndicator.getRecipientId());

        // Send typing indicator to recipient
        String destination = "/user/" + typingIndicator.getRecipientId() + "/queue/typing";
        messagingTemplate.convertAndSend(destination, typingIndicator);
    }

    // Helper method to convert DTO to Entity (simplified version)
    private ChatMessage convertToEntity(ChatMessageDto dto) {
        return ChatMessage.builder()
                .id(dto.getId())
                .chatId(dto.getChatId())
                .senderId(dto.getSenderId())
                .recipientId(dto.getRecipientId())
                .content(dto.getContent())
                .timestamp(dto.getTimestamp())
                .status(dto.getStatus())
                .build();
    }

    /**
     * DTO for typing indicators
     */
    public static class TypingIndicatorDto {
        private Integer senderId;
        private Integer recipientId;
        private boolean typing;

        // Getters and setters
        public Integer getSenderId() { return senderId; }
        public void setSenderId(Integer senderId) { this.senderId = senderId; }

        public Integer getRecipientId() { return recipientId; }
        public void setRecipientId(Integer recipientId) { this.recipientId = recipientId; }

        public boolean isTyping() { return typing; }
        public void setTyping(boolean typing) { this.typing = typing; }
    }
}
