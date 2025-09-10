package com.example.admin_service.dto.request;


import com.example.admin_service.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStatusNotification {
    private String email;
    private Status status;
}
