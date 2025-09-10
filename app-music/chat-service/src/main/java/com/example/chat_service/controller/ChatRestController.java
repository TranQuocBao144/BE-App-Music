package com.example.chat_service.controller;


import com.example.chat_service.dto.ChatMessageDto;
import com.example.chat_service.dto.UserDto;
import com.example.chat_service.enums.Role;
import com.example.chat_service.service.ChatService;
import com.example.chat_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for chat-related HTTP endpoints
 * Provides APIs for chat history, user management, and other non-real-time operations
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

    private final ChatService chatService;
    private final UserService userService;

    /**
     * Get chat history between two users
     * GET /api/chat/history
     *
     * @param senderId first user ID
     * @param recipientId second user ID
     * @param page page number (default: 0)
     * @param size page size (default: 20)
     * @return paginated chat messages
     */
    @GetMapping("/history")
    public ResponseEntity<Page<ChatMessageDto>> getChatHistory(
            @RequestParam Integer senderId,
            @RequestParam Integer recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Retrieving chat history between users {} and {}, page={}, size={}",
                senderId, recipientId, page, size);

        Page<ChatMessageDto> messages = chatService.findChatMessages(senderId, recipientId, page, size);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get all conversations for a user
     * GET /api/chat/conversations/{userId}
     *
     * @param userId the user ID
     * @return list of conversations with latest messages
     */
    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<ChatMessageDto>> getUserConversations(@PathVariable Integer userId) {
        log.info("Retrieving conversations for user {}", userId);

        List<ChatMessageDto> conversations = chatService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    /**
     * Get active users by role
     * GET /api/chat/users
     *
     * @param role the role to filter by (admin, artist)
     * @return list of active users with the specified role
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsersByRole(@RequestParam String role) {
        log.info("Retrieving active users with role: {}", role);

        try {
            Role roleEnum = Role.valueOf(role.toLowerCase());
            List<UserDto> users = userService.findActiveUsersByRole(roleEnum);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid role specified: {}", role);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get user information
     * GET /api/chat/users/{userId}
     *
     * @param userId the user ID
     * @return user information
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
        log.info("Retrieving user information for user {}", userId);

        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mark messages as read
     * PUT /api/chat/messages/read
     *
     * @param chatId the chat ID
     * @param userId the user ID who read the messages
     * @return success response
     */
    @PutMapping("/messages/read")
    public ResponseEntity<Void> markMessagesAsRead(
            @RequestParam String chatId,
            @RequestParam Integer userId) {

        log.info("Marking messages as read for chat {} by user {}", chatId, userId);

        chatService.markMessagesAsRead(chatId, userId);
        return ResponseEntity.ok().build();
    }
}
