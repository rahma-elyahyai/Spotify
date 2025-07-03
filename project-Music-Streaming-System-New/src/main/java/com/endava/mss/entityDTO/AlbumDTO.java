package com.endava.mss.entityDTO;

import java.time.LocalDate;
import java.util.List;

import com.endava.mss.entities.Artist;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbumDTO {
	
	private Long id;
	private String title;
	private LocalDate releaseDate;
	private byte[] coverImage;
	private Long artistId;
	private List<SongDTO> songs;
	
}
