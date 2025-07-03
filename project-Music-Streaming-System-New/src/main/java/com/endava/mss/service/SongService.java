package com.endava.mss.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;

import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.entities.Song;
import com.endava.mss.entityDTO.SongDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface SongService {

	public ResponseEntity<APIGenricResponse> getAllApprovedSongs();

	public ResponseEntity<APIGenricResponse> getSongById(Long id);

	public ResponseEntity<APIGenricResponse> saveSong(SongDTO songDTO);

	public ResponseEntity<APIGenricResponse>  deleteSong(Long id);

	public ResponseEntity<APIGenricResponse>  approveSongs(Long Id, String action);

	public ResponseEntity<APIGenricResponse>  getTopFiveApprovedSongs();

	public ResponseEntity<APIGenricResponse>  playCount(Long id);

	public ResponseEntity<APIGenricResponse>  getFilteredSongs(String genre, String language);

	public ResponseEntity<APIGenricResponse>  getAllGenres();

	public ResponseEntity<APIGenricResponse>  getAllLanguages();
	
	public ResponseEntity<APIGenricResponse>  autoSleep(Long duration) throws InterruptedException, ExecutionException;
	
	public ResponseEntity<APIGenricResponse>  searchSongsAndArtists(String term, int page, int size);
	
	public ResponseEntity<APIGenricResponse> songRequests(String status, int page,int size);
	
	public ResponseEntity<APIGenricResponse>  voiceSearch(String command);
	
	public List<SongDTO> findAllSongs();
	
	public ResponseEntity<APIGenricResponse>  editSong(Long id, SongDTO songDTO); 
	
	public ResponseEntity<byte[]> streamSong( Long id, HttpServletRequest request);
	
	public ResponseEntity<byte[]> songImage(Long id);

	
}
