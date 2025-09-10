package com.example.artist_service.service;

import com.example.artist_service.dto.request.SongCreationRequest;
import com.example.artist_service.dto.response.songViews;
import com.example.artist_service.entity.Song;
import com.example.artist_service.mapper.SongMapper;
import com.example.artist_service.repository.SongRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class SongService {
    SongMapper songMapper;
    SongRepository songRepository;
    public Song createSong(SongCreationRequest request) {
        Song song = songMapper.toSong(request);
        song.setSongname(request.getSongname());
        song.setSongimage(request.getSongimage());
        song.setAlbumid(request.getAlbumid());
        song.setArtistid(request.getArtistid());
        song.setLinksong(request.getLinksong());
        song.setTypeid(request.getTypeid());
        song.setLinklrc(request.getLinklrc());

        song = songRepository.save(song);
        return song;
    }


    public List<songViews> getViewCountsByArtist(Integer artistId) {
        return songRepository.findViewCountsByArtist(artistId);
    }


    public List<Song> getSongsByArtist(Integer artistId) {
        return songRepository.findByArtistid(artistId);
    }
}
