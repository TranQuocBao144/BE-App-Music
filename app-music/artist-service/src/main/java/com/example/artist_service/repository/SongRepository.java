package com.example.artist_service.repository;


import com.example.artist_service.dto.response.songViews;
import com.example.artist_service.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    @Query("""
        SELECT new com.example.artist_service.dto.response.songViews(
            s.songname, s.views
        )
        FROM Song s
        WHERE s.artistid = :artistId
        """)
    List<songViews> findViewCountsByArtist(
            @Param("artistId") Integer artistId
    );

    List<Song> findByArtistid(Integer artistid);

}
