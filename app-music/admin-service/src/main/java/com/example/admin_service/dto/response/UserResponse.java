package com.example.admin_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     int userid;
     String username;
     String email;
     String password;
     String role;
     String phone;
     String status;
     Date createat;
}
