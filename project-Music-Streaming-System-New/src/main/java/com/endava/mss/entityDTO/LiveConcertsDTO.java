package com.endava.mss.entityDTO;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LiveConcertsDTO {


	private Long Id;
	
	private LocalDate date ;
	
	private String location ;
	
	private Long artistId;
}
