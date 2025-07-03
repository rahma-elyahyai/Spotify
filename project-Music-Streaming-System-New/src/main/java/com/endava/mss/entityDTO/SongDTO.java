package com.endava.mss.entityDTO;


import java.time.LocalDate;

import com.endava.mss.entities.Song.SongStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private Long id;
    private String title;
    private String genre;
    private String language;
    private Long playCount;
    private byte[] coverImage;
    private LocalDate releaseDate;
    private SongStatus status;
    private Long favorite;
    private String lyrics;
    private byte[] mp3File;
    private Long artistId;
    private Long albumId;
    private Long listenedDuration;
}
