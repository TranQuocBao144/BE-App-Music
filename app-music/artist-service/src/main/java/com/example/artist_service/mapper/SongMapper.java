package com.example.artist_service.mapper;


import com.example.artist_service.dto.request.SongCreationRequest;
import com.example.artist_service.entity.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {
    Song toSong(SongCreationRequest request);
}
