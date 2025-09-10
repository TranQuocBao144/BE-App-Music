package com.example.artist_service.controller;


import com.example.artist_service.dto.request.SongCreationRequest;
import com.example.artist_service.dto.response.ApiRespone;
import com.example.artist_service.dto.response.songViews;
import com.example.artist_service.entity.Song;
import com.example.artist_service.service.SongService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ArtistController {
    SongService songService;
    @PostMapping
    ApiRespone<Song> createSong(@RequestBody SongCreationRequest request) {
        ApiRespone<Song> apiRespone = new ApiRespone<>();
        apiRespone.setResult(songService.createSong(request));
        return apiRespone;
    }


    @GetMapping("/{artistId}/views")
    ApiRespone<List<songViews>> getAllSongs(@PathVariable Integer artistId) {
        ApiRespone<List<songViews>> apiRespone = new ApiRespone<>();
        apiRespone.setResult(songService.getViewCountsByArtist(artistId));
        return apiRespone;
    }

    @GetMapping("/{artistId}")
    public List<Song> getSongs(@PathVariable Integer artistId) {
        return songService.getSongsByArtist(artistId);
    }
}
