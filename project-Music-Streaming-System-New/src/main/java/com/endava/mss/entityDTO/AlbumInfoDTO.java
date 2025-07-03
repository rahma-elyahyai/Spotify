package com.endava.mss.entityDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class AlbumInfoDTO {

	private Long id;
	private String title;
	private Long artistId;
	private List<SongInfoDTO> songs;
}
