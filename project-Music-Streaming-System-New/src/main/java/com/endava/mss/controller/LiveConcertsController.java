package com.endava.mss.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.endava.mss.constantFiles.LiveConcertConstants;
import com.endava.mss.entityDTO.LiveConcertsDTO;
import com.endava.mss.service.LiveConcertService;

@RestController
@RequestMapping(LiveConcertConstants.BASE_CONCERT_URL)
@CrossOrigin(origins = "*")
public class LiveConcertsController {

	
	private final LiveConcertService liveConcertService;

	public LiveConcertsController(LiveConcertService liveConcertService) {
		this.liveConcertService = liveConcertService;
	}

	@PostMapping(LiveConcertConstants.CREATE_CONCERT)
	public ResponseEntity<LiveConcertsDTO> createLiveConcert(@RequestBody LiveConcertsDTO liveConcertDTO) {

		LiveConcertsDTO createdConcert = liveConcertService.newLiveConcert(liveConcertDTO);
		return new ResponseEntity<>(createdConcert, HttpStatus.CREATED);

	}

	@DeleteMapping(LiveConcertConstants.CANCEL_CONCERT)
	public ResponseEntity<String> cancelLiveConcert(@PathVariable Long concertId) {

		liveConcertService.cancelLiveConcert(concertId);
		return new ResponseEntity<>(LiveConcertConstants.CONCERT_CANCELLED, HttpStatus.OK);

	}
}
