package com.example.admin_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public NewTopic userStatusTopic() {
        return new NewTopic("user-status-topic", 1, (short) 1);
    }
}