package com.endava.mss.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.mss.constantFiles.SongQueueConstants;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.entityDTO.SongQueueDTO;
import com.endava.mss.service.SongQueueService;

@RestController
@RequestMapping(SongQueueConstants.SONG_QUEUE_BASE_URL)
public class SongQueueController {

	private final SongQueueService songQueueService;

	public SongQueueController(SongQueueService songQueueService) {
		this.songQueueService = songQueueService;
	}

	@PostMapping(SongQueueConstants.ADD_SONG_TO_QUEUE)
	public ResponseEntity<SongQueueDTO> addSongtoQueue(@RequestBody SongQueueDTO songQueueDTO) {

		SongQueueDTO songQueueAdded = songQueueService.addSongtoQueue(songQueueDTO);
		return new ResponseEntity<>(songQueueAdded, HttpStatus.CREATED);

	}

	@GetMapping(SongQueueConstants.GET_USER_SONG_QUEUE)
	public ResponseEntity<List<SongQueueDTO>> getUserSongQueue(@PathVariable Long userId) {

		List<SongQueueDTO> userSongQueue = songQueueService.getAllSongQueues(userId);
		return new ResponseEntity<>(userSongQueue, HttpStatus.OK);

	}

	@DeleteMapping(SongQueueConstants.CLEAR_USER_SONG_QUEUE)
	public ResponseEntity<String> clearUserSongQueue(@PathVariable Long userId) {

		songQueueService.clearSongQueues(userId);
		return new ResponseEntity<>(SongQueueConstants.CLEARED_USER_SONG_QUEUE_MESSAGE, HttpStatus.OK);

	}

	@GetMapping(SongQueueConstants.GET_NEXT_SONG)
	public ResponseEntity<SongDTO> getNextSong(@PathVariable Long userId) {

		SongDTO nextSong = songQueueService.getNextSongFromSongQueue(userId);
		return new ResponseEntity<>(nextSong, HttpStatus.OK);
	}

	@GetMapping(SongQueueConstants.GET_PREVIOUS_SONG)
	public ResponseEntity<SongDTO> getPreviousSong(@PathVariable Long userId) {
		SongDTO previousSong = songQueueService.getPreviousSongFromSongQueue(userId);
		return new ResponseEntity<>(previousSong, HttpStatus.OK);
	}

	@GetMapping(SongQueueConstants.SHUFFLE_SONGS)
	public ResponseEntity<List<SongQueueDTO>> shuffleSongs(@PathVariable Long userId) {

		List<SongQueueDTO> shuffledSongs = songQueueService.shuffleSongs(userId);

		return new ResponseEntity<>(shuffledSongs, HttpStatus.OK);
	}

	@GetMapping(SongQueueConstants.GET_ALL_SONGS)
	public ResponseEntity<List<SongQueueDTO>> getAllSongs(@PathVariable Long userId) {
		List<SongQueueDTO> userSongQueue = songQueueService.getAllSongQueues(userId);

		return new ResponseEntity<>(userSongQueue, HttpStatus.OK);
	}

	@PutMapping(SongQueueConstants.REORDER_QUEUE)
	public ResponseEntity<List<SongQueueDTO>> reorderQueue(@RequestBody List<SongQueueDTO> songQueueDTO,
			@PathVariable Long userId) {
		List<SongQueueDTO> reorderedSongQueue = songQueueService.reorderSongQueue(userId, songQueueDTO);

		return new ResponseEntity<>(reorderedSongQueue, HttpStatus.OK);
	}

	@DeleteMapping(SongQueueConstants.DELETE_SONG_QUEUE)
	public ResponseEntity<String> deleteSongQueue(@PathVariable Long songQueueId) {

		songQueueService.deleteSongInSongQueue(songQueueId);
		return new ResponseEntity<>(SongQueueConstants.DELETE_SONG_IN_SONG_QUEUE_MESSAGE, HttpStatus.OK);

	}

}
