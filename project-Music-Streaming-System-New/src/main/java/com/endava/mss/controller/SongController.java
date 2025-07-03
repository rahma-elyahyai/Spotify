package com.endava.mss.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.endava.mss.constantFiles.SongConstants;
import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.service.SongService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(SongConstants.SONG_BASE_URL)
public class SongController {

	private final SongService songService;

	public SongController(SongService songService) {
		this.songService = songService;
	}

	@GetMapping(SongConstants.STREAM_SONG)
	public ResponseEntity<byte[]> streamSong(@PathVariable Long id, HttpServletRequest request) {

		return songService.streamSong(id, request);
	}

	@GetMapping(SongConstants.GET_SONG_IMAGE)
	public ResponseEntity<byte[]> getSongImageById(@PathVariable Long id) {
		return songService.songImage(id);
	}

	@PutMapping
	public ResponseEntity<APIGenricResponse> updateSong(@RequestParam Long songId, @RequestParam String action) {
		return songService.approveSongs(songId, action);
	}

	@GetMapping(SongConstants.GET_ALL_APPROVED_SONGS)
	public ResponseEntity<APIGenricResponse> getAllApprovedSongs() {
		return songService.getAllApprovedSongs();
	}

	@GetMapping(SongConstants.GET_TOP_FIVE_APPROVED_SONGS)
	public ResponseEntity<APIGenricResponse> getTopFiveApprovedSongs() {
		return songService.getTopFiveApprovedSongs();
	}

	@DeleteMapping(SongConstants.DELETE_SONG)
	public ResponseEntity<APIGenricResponse> deleteSong(@PathVariable Long songId) {

		return songService.deleteSong(songId);

	}

	@PutMapping(SongConstants.UPDATE_PLAY_COUNT)
	public ResponseEntity<APIGenricResponse> updatePlayCount(@RequestParam Long id) {
		return songService.playCount(id);
	}

	@GetMapping(SongConstants.FILTER_SONGS)
	public ResponseEntity<APIGenricResponse> getFilteredSongs(@RequestParam(required = false) String genre,
			@RequestParam(required = false) String language) {
		return songService.getFilteredSongs(genre, language);
	}

	@GetMapping(SongConstants.GET_GENRES)
	public ResponseEntity<APIGenricResponse> getAllGenres() {
		return songService.getAllGenres();
	}

	@GetMapping(SongConstants.GET_LANGUAGES)
	public ResponseEntity<APIGenricResponse> getAllLanguages() {
		return songService.getAllLanguages();

	}

	@GetMapping(SongConstants.AUTO_SLEEP)
	public ResponseEntity<APIGenricResponse> autoSleep(@PathVariable Long duration)
			throws InterruptedException, ExecutionException {
		return songService.autoSleep(60000l * duration);

	}

	@GetMapping(SongConstants.SEARCH_SONGS_ARTISTS)
	public ResponseEntity<APIGenricResponse> search(@PathVariable String term,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
		return songService.searchSongsAndArtists(term, page, size);

	}

	@GetMapping(SongConstants.VOICE_SEARCH_SONGS_ARTISTS)
	public ResponseEntity<APIGenricResponse> voiceSearch(@PathVariable String term) {
		return songService.voiceSearch(term);

	}
	
	@GetMapping("/songRequests")
	public ResponseEntity<APIGenricResponse> songRequests(@RequestParam String status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size)
	{
		return songService.songRequests(status, page, size);
	}

	@PostMapping
	public ResponseEntity<APIGenricResponse> createSong(@RequestParam String title, @RequestParam String genre,
			@RequestParam String language, @RequestParam Long playCount, @RequestParam MultipartFile coverImage,
			@RequestParam LocalDate releaseDate, @RequestParam MultipartFile mp3File, @RequestParam Long favorite,
			@RequestParam Long artistId, @RequestParam String lyrics, @RequestParam(required = false) Long albumId) {
		SongDTO songDTO = SongDTO.builder().title(title).genre(genre).language(language).playCount(playCount)
				.releaseDate(releaseDate).artistId(artistId).favorite(favorite).lyrics(lyrics).build();

		if (coverImage != null && !coverImage.isEmpty()) {

			byte[] coverImageBytes = convertFileToByteArray(coverImage);
			songDTO.setCoverImage(coverImageBytes);
		}
		if (mp3File != null && !mp3File.isEmpty()) {

			byte[] mp3FileBytes = convertFileToByteArray(mp3File);
			songDTO.setMp3File(mp3FileBytes);
		}

		if (albumId != null) {
			songDTO.setAlbumId(albumId);
		}

		return songService.saveSong(songDTO);

	}

	@PutMapping(SongConstants.EDIT_SONGS)
	public ResponseEntity<APIGenricResponse> editSong(@PathVariable Long id, @RequestParam String title,
			@RequestParam String genre, @RequestParam String language,
			@RequestParam(required = false) MultipartFile coverImage,
			@RequestParam(required = false) MultipartFile mp3File, @RequestParam String lyrics) {

		SongDTO existingSong = (SongDTO) songService.getSongById(id).getBody().getBody();
		if (existingSong == null) {
			return ResponseEntity.notFound().build();
		}

		existingSong.setTitle(title);
		existingSong.setGenre(genre);
		existingSong.setLanguage(language);
		existingSong.setLyrics(lyrics);

		if (coverImage != null && !coverImage.isEmpty()) {
			existingSong.setCoverImage(convertFileToByteArray(coverImage));
		}

		if (mp3File != null && !mp3File.isEmpty()) {
			existingSong.setMp3File(convertFileToByteArray(mp3File));
		}

		return songService.editSong(id, existingSong);
	}
	
	private byte[] convertFileToByteArray(MultipartFile file) {
		try {
			return file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
	
	@GetMapping("/AllSongs")
	public List<SongDTO>getAllSongs()
	{
		return songService.findAllSongs();
	}

}
