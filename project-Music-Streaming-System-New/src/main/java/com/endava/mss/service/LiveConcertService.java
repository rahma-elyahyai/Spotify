package com.endava.mss.service;

import com.endava.mss.entityDTO.LiveConcertsDTO;

public interface LiveConcertService {

	LiveConcertsDTO newLiveConcert(LiveConcertsDTO liveConcertDTO);
	
	void cancelLiveConcert(Long liveConcertId);
	
}
