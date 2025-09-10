package com.example.notification_service.services;

import com.example.notification_service.dto.UserStatusNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusListener {

    private final JavaMailSender mailSender;

    @KafkaListener(topics = "user-status-topic", groupId = "notification-group")
    public void handleUserStatusUpdate(UserStatusNotification event) {
        System.out.println("Received event: " + event);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("Cập nhật trạng thái tài khoản");
        message.setText("Tài khoản của bạn đã được cập nhật trạng thái: " + event.getStatus());

        mailSender.send(message);
        System.out.println("Email sent to: " + event.getEmail());
        try {
            mailSender.send(message);
            System.out.println(" Email sent to: " + event.getEmail());
        } catch (Exception e) {
            System.err.println(" Failed to send email to: " + event.getEmail());
            e.printStackTrace();
        }
    }
}

