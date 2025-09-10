package com.example.artist_service.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SongCreationRequest {
    String songname;
    String songimage;
    int albumid;
    int artistid;
    String linksong;
    int typeid;
    String linklrc;
}
