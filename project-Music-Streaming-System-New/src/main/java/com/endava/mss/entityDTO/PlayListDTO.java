package com.endava.mss.entityDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayListDTO {

	private Long id;

	private String playListName;

	private Long userId;

	private List<SongInfoDTO> songs;
	
	private String userName;

}
