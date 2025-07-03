package com.endava.mss.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.endava.mss.constantFiles.PlaylistConstants;
import com.endava.mss.entityDTO.PlayListDTO;
import com.endava.mss.service.PlayListService;

@RestController
@RequestMapping(PlaylistConstants.BASE_PLAYLIST_URL)
public class PlayListController {

	private final PlayListService playListService;

	public PlayListController(PlayListService playListService) {

		this.playListService = playListService;
	}

	@PostMapping
	public ResponseEntity<PlayListDTO> createPlayList(@RequestBody PlayListDTO playListDTO) {
		PlayListDTO createdPlayList = playListService.createPlayList(playListDTO);
		return new ResponseEntity<>(createdPlayList, HttpStatus.CREATED);
	}

	@PostMapping(PlaylistConstants.ADD_SONG_TO_PLAYLIST)
	public ResponseEntity<PlayListDTO> addSongToPlayList(@RequestParam Long playListId,
			@RequestParam Long songId) {
		PlayListDTO updatedPlayList = playListService.addSongtoPlayList(playListId, songId);
		return new ResponseEntity<>(updatedPlayList, HttpStatus.OK);
	}

	@GetMapping(PlaylistConstants.FETCH_PLAYLIST)
	public ResponseEntity<PlayListDTO> fetchPlayList(@PathVariable Long playListId) {
		PlayListDTO playListDTO = playListService.fetchPlayList(playListId);
		return new ResponseEntity<>(playListDTO, HttpStatus.OK);
	}
	
	@GetMapping(PlaylistConstants.FETCH_SHARED_PLAYLIST)
	public ResponseEntity<PlayListDTO> fetchSharedPlayList(@PathVariable Long playListId) {
		PlayListDTO playListDTO = playListService.fetchPlayList(playListId);
		return new ResponseEntity<>(playListDTO, HttpStatus.OK);
	}

	@PostMapping(PlaylistConstants.PLAY_PLAYLIST)
	public ResponseEntity<PlayListDTO> autoPlayPlayList(@RequestBody PlayListDTO playListDTO) {
		PlayListDTO userPlayListDTO = playListService.playSongsfromPlaylist(playListDTO);
		return new ResponseEntity<>(userPlayListDTO, HttpStatus.OK);
	}

	@DeleteMapping(PlaylistConstants.DELETE_PLAYLIST)
	public ResponseEntity<String> deletePlayList(@PathVariable Long playListId) {

		playListService.deletePlayList(playListId);
		return new ResponseEntity<>(PlaylistConstants.DELETED_PLAYLIST_MESSAGE, HttpStatus.NO_CONTENT);

	}

	@DeleteMapping(PlaylistConstants.DELETE_SONG_FROM_PLAYLIST)
	public ResponseEntity<String> deleteSongFromPlayList(@PathVariable Long playListId, @PathVariable Long songId) {

		playListService.deleteSongFromPlayList(playListId, songId);
		return new ResponseEntity<>(PlaylistConstants.DELETE_SONG_FROM_PLAYLIST, HttpStatus.OK);
	}
	
	@PostMapping(PlaylistConstants.SAVE_SHARED_PLAYLIST)
	public ResponseEntity<PlayListDTO>saveSharedPlaylist(@PathVariable Long playListId,@PathVariable String token)
	{
		PlayListDTO savedPlayListDTO = playListService.saveSharedPlaylist(playListId, token);
		return new ResponseEntity<>(savedPlayListDTO,HttpStatus.OK);
	}
}
