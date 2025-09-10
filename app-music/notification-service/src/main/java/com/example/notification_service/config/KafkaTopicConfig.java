package com.example.notification_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic userStatusTopic() {
        return new NewTopic("user-status-topic", 1, (short) 1);
    }
}
