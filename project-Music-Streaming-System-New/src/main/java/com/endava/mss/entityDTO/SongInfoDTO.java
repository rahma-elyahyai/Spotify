package com.endava.mss.entityDTO;

import java.time.LocalDate;

import com.endava.mss.entities.Song.SongStatus;

public record SongInfoDTO(Long id, String title,Long playCount, SongStatus status,
		 Long artistId, String lyrics, String genre, String language,
		 Long albumId,LocalDate releaseDate) {
	
	
}