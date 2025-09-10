package com.example.admin_service.entity;

import com.example.admin_service.enums.Role;
import com.example.admin_service.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
     int userid;

     String username;
     String email;
     String password;
    @Enumerated(EnumType.STRING)
    Role role;
    String phone;
    @Enumerated(EnumType.STRING)
    Status status;
    String deviceid;
    @Column(name="created_at")
    LocalDateTime createat;
}
