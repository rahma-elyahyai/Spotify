package com.endava.mss.entityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongQueueDTO {

	private Long id ;
	private Long currentPosition;
	private Long userId;	
	private Long songId;
	private String songTitle;
}
