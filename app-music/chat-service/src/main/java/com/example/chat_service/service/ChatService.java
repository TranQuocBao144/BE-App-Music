package com.example.chat_service.service;

import com.example.chat_service.dto.ChatMessageDto;
import com.example.chat_service.dto.ChatNotificationDto;
import com.example.chat_service.entity.ChatMessage;
import com.example.chat_service.entity.User;
import com.example.chat_service.enums.Role;
import com.example.chat_service.exception.AppException;
import com.example.chat_service.exception.ErrolCode;
import com.example.chat_service.repository.ChatMessageRepository;
import com.example.chat_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class containing all business logic for chat functionality
 * Handles message validation, storage, and retrieval with role-based restrictions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    /**
     * Save a chat message with business logic validation
     * Enforces role-based messaging rules:
     * - Artists can only send to Admins
     * - Admins can send to specific Artists
     *
     * @param messageDto the message data to save
     * @return saved message DTO
     * //@throws ChatServiceException if validation fails
     */
    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto messageDto) {
        log.debug("Saving message from user {} to user {}",
                messageDto.getSenderId(), messageDto.getRecipientId());

        // Validate message content
        validateMessageContent(messageDto);

        // Validate users exist and are active
        User sender = validateUser(messageDto.getSenderId());
        User recipient = validateUser(messageDto.getRecipientId());

        // Validate role-based messaging rules
        validateMessagingRules(sender, recipient);

        // Generate chat ID
        String chatId = generateChatId(messageDto.getSenderId(), messageDto.getRecipientId());

        // Create and save message entity
        ChatMessage message = ChatMessage.builder()
                .chatId(chatId)
                .senderId(messageDto.getSenderId())
                .recipientId(messageDto.getRecipientId())
                .content(messageDto.getContent())
                .timestamp(LocalDateTime.now())
                .status("sent")
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);
        log.info("Message saved successfully with ID: {}", savedMessage.getId());

        // Convert back to DTO
        return convertToDto(savedMessage, sender, recipient);
    }

    /**
     * Retrieve chat history between two users with pagination
     *
     * @param senderId first user ID
     * @param recipientId second user ID
     * @param page page number (0-based)
     * @param size page size
     * @return paginated list of messages
     */
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> findChatMessages(Integer senderId, Integer recipientId,
                                                 int page, int size) {
        log.debug("Retrieving chat history between users {} and {}", senderId, recipientId);

        // Validate users exist
        validateUser(senderId);
        validateUser(recipientId);

        Pageable pageable = PageRequest.of(page, size);

        // Find messages bidirectionally
        Page<ChatMessage> messages = chatMessageRepository.findChatMessages(
                senderId, recipientId, recipientId, senderId, pageable);

        log.debug("Found {} messages in chat history", messages.getTotalElements());

        // Convert to DTOs
        return messages.map(this::convertToDto);
    }

    /**
     * Get all conversations for a specific user
     * Returns the latest message from each conversation
     *
     * @param userId the user ID
     * @return list of latest messages from each conversation
     */
    @Transactional(readOnly = true)
    public List<ChatMessageDto> getUserConversations(Integer userId) {
        log.debug("Retrieving conversations for user {}", userId);

        validateUser(userId);

        List<ChatMessage> latestMessages = chatMessageRepository.findLatestMessagesForUser(userId);

        return latestMessages.stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Create chat notification DTO
     *
     * @param message the chat message
     * @return notification DTO
     */
    public ChatNotificationDto createNotification(ChatMessage message) {
        Optional<User> sender = userRepository.findById(message.getSenderId());
        Long unreadCount = chatMessageRepository.countUnreadMessages(
                message.getRecipientId(), message.getSenderId());

        return ChatNotificationDto.builder()
                .messageId(message.getId())
                .chatId(message.getChatId())
                .senderId(message.getSenderId())
                .senderUsername(sender.map(User::getUsername).orElse("Unknown"))
                .content(message.getContent())
                .unreadCount(unreadCount.intValue())
                .build();
    }

    /**
     * Mark messages as read
     *
     * @param chatId the chat ID
     * @param userId the user ID who read the messages
     */
    @Transactional
    public void markMessagesAsRead(String chatId, Integer userId) {
        log.debug("Marking messages as read for chat {} by user {}", chatId, userId);

        // Implementation would update message status to 'read'
        // This is a placeholder for the actual implementation
    }

    // Private helper methods

    private void validateMessageContent(ChatMessageDto messageDto) {
        if (messageDto.getContent() == null || messageDto.getContent().trim().isEmpty()) {
            throw new AppException(ErrolCode.MESSAGE_CAN_NOT_EMPTY);
        }
        if (messageDto.getContent().length() > 2000) {
            throw new AppException(ErrolCode.MESAGE_TOO_LONG);
        }
        if (messageDto.getSenderId() == null || messageDto.getRecipientId() == null) {
            throw new AppException(ErrolCode.MESSAGE_SENDER);
        }
        if (messageDto.getSenderId().equals(messageDto.getRecipientId())) {
            throw new AppException(ErrolCode.MESSAGE_NOT_SENT);
        }
    }

    private User validateUser(Integer userId) {
        return userRepository.findById(userId)
                .filter(user -> "active".equals(user.getStatus()))
                .orElseThrow(() -> new RuntimeException("User not found or inactive: " + userId));
    }

    private void validateMessagingRules(User sender, User recipient) {
        Role senderRole = Role.valueOf(sender.getRole());
        Role recipientRole = Role.valueOf(recipient.getRole());

        // Artists can only send to Admins
        if (senderRole == Role.artist && recipientRole != Role.admin) {
            throw new AppException(ErrolCode.MESSAGE_ARTIS_SENT);
        }

        // Admins can send to Artists (no restriction)
        if (senderRole == Role.admin && recipientRole != Role.artist) {
            throw new AppException(ErrolCode.MESSAGE_ADMIN_SENT);
        }

        log.debug("Message routing validated: {} -> {}", senderRole, recipientRole);
    }

    private String generateChatId(Integer userId1, Integer userId2) {
        // Create consistent chat ID regardless of message direction
        // Format: smaller_id-larger_id
        int smaller = Math.min(userId1, userId2);
        int larger = Math.max(userId1, userId2);
        return smaller + "-" + larger;
    }

    private ChatMessageDto convertToDto(ChatMessage message) {
        return convertToDto(message, null, null);
    }

    private ChatMessageDto convertToDto(ChatMessage message, User sender, User recipient) {
        ChatMessageDto.ChatMessageDtoBuilder builder = ChatMessageDto.builder()
                .id(message.getId())
                .chatId(message.getChatId())
                .senderId(message.getSenderId())
                .recipientId(message.getRecipientId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .status(message.getStatus());

        // Add username information if provided
        if (sender != null) {
            builder.senderUsername(sender.getUsername());
        }
        if (recipient != null) {
            builder.recipientUsername(recipient.getUsername());
        }

        return builder.build();
    }
}
