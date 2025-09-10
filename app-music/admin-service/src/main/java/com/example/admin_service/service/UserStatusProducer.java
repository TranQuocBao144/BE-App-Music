package com.example.admin_service.service;

import com.example.admin_service.dto.request.UserStatusNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusProducer {
    private final KafkaTemplate<String, UserStatusNotification> kafkaTemplate;
    private static final String TOPIC = "user-status-topic";

    public void sendStatusNotification(UserStatusNotification payload) {
        // dùng email làm key cho dễ trace
        kafkaTemplate.send(TOPIC, payload.getEmail(), payload);
        log.info("📤 Gửi Kafka: {}", payload);
        log.info(" Sending Kafka message to topic [{}]: {}", TOPIC, payload);
    }
}