package com.example.chat_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration class for real-time chat functionality
 * Configures STOMP messaging protocol over WebSocket
 *
 * Key Configuration Points:
 * - Endpoint: /ws for WebSocket connections
 * - Application prefix: /app for messages from client to server
 * - User destination prefix: /user for private messaging
 * - Topic prefix: /topic for broadcast messages (not used in this private chat system)
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configure message broker options
     * - /user prefix enables private messaging to specific users
     * - /topic prefix for broadcasting (optional for future use)
     * - /app prefix for application-specific message mapping
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        log.info("Configuring WebSocket message broker");

        // Enable simple broker for user-specific messaging and topic broadcasting
        config.enableSimpleBroker("/user", "/topic");

        // Set application destination prefix for client-to-server messages
        config.setApplicationDestinationPrefixes("/app");

        // Set user destination prefix for private messaging
        config.setUserDestinationPrefix("/user");

        log.info("Message broker configured successfully");
    }

    /**
     * Register STOMP endpoints for WebSocket connections
     * - /ws is the main endpoint for WebSocket handshake
     * - SockJS fallback is enabled for browsers that don't support WebSocket
     * - CORS is allowed for development (configure properly for production)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Registering STOMP endpoints");

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Configure properly for production
                .withSockJS(); // Enable SockJS fallback for older browsers

        // Also register without SockJS for native WebSocket support
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

        log.info("STOMP endpoints registered successfully");
    }
}
