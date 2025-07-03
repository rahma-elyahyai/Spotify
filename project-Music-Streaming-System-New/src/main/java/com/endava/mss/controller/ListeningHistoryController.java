package com.endava.mss.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.mss.constantFiles.ListeningHistoryConstants;
import com.endava.mss.entityDTO.ListeningHistoryDTO;
import com.endava.mss.entityDTO.SongInfoDTO;
import com.endava.mss.service.ListeningHistoryService;

@RestController
@RequestMapping(ListeningHistoryConstants.BASE_LISTENING_HISTORY_URL)
public class ListeningHistoryController {


	private final ListeningHistoryService listeningHistoryService;
	
	public ListeningHistoryController(ListeningHistoryService listeningHistoryService) {
		
		this.listeningHistoryService = listeningHistoryService;
	}

	@PostMapping
	public ResponseEntity<ListeningHistoryDTO> addSongListeningHistory(
			@RequestBody ListeningHistoryDTO listeningHistoryDTO) {

		ListeningHistoryDTO userListeningHistory = listeningHistoryService.addSongListeningHistory(listeningHistoryDTO);
		return new ResponseEntity<>(userListeningHistory, HttpStatus.CREATED);

	}

	@GetMapping(ListeningHistoryConstants.FETCH_LISTENING_HISTORY_URL)
	public ResponseEntity<	List<SongInfoDTO>> fetchListeningHistoryOfUser(@PathVariable Long userId) {

		List<SongInfoDTO> listeningHistoryList = listeningHistoryService.fetchListeningHistoryOfUser(userId);
		return new ResponseEntity<>(listeningHistoryList, HttpStatus.OK);

	}

	@DeleteMapping(ListeningHistoryConstants.DELETE_LISTENING_HISTORY_URL)
	public ResponseEntity<String> deletePlayList(@PathVariable Long userId) {

		listeningHistoryService.clearListeningHistory(userId);
		return new ResponseEntity<>(ListeningHistoryConstants.CLEARED_HISTORY_MESSAGE, HttpStatus.NO_CONTENT);

	}
}
