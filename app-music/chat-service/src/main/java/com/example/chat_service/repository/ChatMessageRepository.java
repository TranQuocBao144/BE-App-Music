package com.example.chat_service.repository;

import com.example.chat_service.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ChatMessage entity
 * Provides methods to manage chat message data with advanced querying capabilities
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Find chat messages between two users ordered by timestamp
     * @param chatId the chat ID that groups messages between two users
     * @param pageable pagination information
     * @return Page of chat messages
     */
    Page<ChatMessage> findByChatIdOrderByTimestampDesc(String chatId, Pageable pageable);

    /**
     * Find chat messages between two users (bidirectional)
     * This method finds messages where user1 sent to user2 OR user2 sent to user1
     * @param senderId first user ID
     * @param recipientId second user ID
     * @param recipientId2 second user ID (for reverse direction)
     * @param senderId2 first user ID (for reverse direction)
     * @param pageable pagination information
     * @return Page of chat messages ordered by timestamp descending
     */
    @Query("SELECT cm FROM ChatMessage cm WHERE " +
            "(cm.senderId = :senderId AND cm.recipientId = :recipientId) OR " +
            "(cm.senderId = :recipientId2 AND cm.recipientId = :senderId2) " +
            "ORDER BY cm.timestamp DESC")
    Page<ChatMessage> findChatMessages(
            @Param("senderId") Integer senderId,
            @Param("recipientId") Integer recipientId,
            @Param("recipientId2") Integer recipientId2,
            @Param("senderId2") Integer senderId2,
            Pageable pageable
    );

    /**
     * Count unread messages for a specific recipient from a specific sender
     * @param recipientId the recipient user ID
     * @param senderId the sender user ID
     * @return count of unread messages
     */
    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.recipientId = :recipientId " +
            "AND cm.senderId = :senderId AND cm.status != 'read'")
    Long countUnreadMessages(@Param("recipientId") Integer recipientId,
                             @Param("senderId") Integer senderId);

    /**
     * Find all distinct chat IDs for a specific user
     * This helps to get all conversations a user is involved in
     * @param userId the user ID
     * @return List of chat IDs
     */
    @Query("SELECT DISTINCT cm.chatId FROM ChatMessage cm WHERE " +
            "cm.senderId = :userId OR cm.recipientId = :userId")
    List<String> findDistinctChatIdsByUserId(@Param("userId") Integer userId);

    /**
     * Find the latest message for each chat involving a specific user
     * Useful for showing chat list with preview of last messages
     * @param userId the user ID
     * @return List of latest messages for each chat
     */
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.timestamp = " +
            "(SELECT MAX(cm2.timestamp) FROM ChatMessage cm2 WHERE cm2.chatId = cm.chatId) " +
            "AND (cm.senderId = :userId OR cm.recipientId = :userId)")
    List<ChatMessage> findLatestMessagesForUser(@Param("userId") Integer userId);
}
