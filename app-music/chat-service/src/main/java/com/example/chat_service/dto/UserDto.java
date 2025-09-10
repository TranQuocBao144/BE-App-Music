package com.example.chat_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for user information transfer
 * Contains only necessary user data for chat functionality
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Integer userid;
    String username;
    String email;
    String role;
    String status;
}
