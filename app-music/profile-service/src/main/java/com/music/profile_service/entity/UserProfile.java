package com.music.profile_service.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;


@Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Node("user_profile")
    public class UserProfile {
        @Id
        @GeneratedValue(generatorClass =  UUIDStringGenerator.class)
        String id;

        @Property("userId")
        int userId;

        String phone;
        String email;
    }
