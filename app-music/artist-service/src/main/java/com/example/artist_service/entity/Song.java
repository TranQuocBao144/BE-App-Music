package com.example.artist_service.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer songid;
    String songname;
    String songimage;
    Integer albumid;
    Integer artistid;
    String linksong;
    Integer typeid;
    Integer views;
    String linklrc;
}
