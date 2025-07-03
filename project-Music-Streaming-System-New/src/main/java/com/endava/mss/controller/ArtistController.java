package com.endava.mss.controller;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.endava.mss.constantFiles.ArtistConstants;
import com.endava.mss.constantFiles.SongConstants;
import com.endava.mss.entities.ETagUtil;
import com.endava.mss.entityDTO.ArtistDTO;
import com.endava.mss.entityDTO.ArtistInfoDTO;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.service.ArtistService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(ArtistConstants.ARTIST_BASE_URL)
public class ArtistController {


	private final ArtistService artistService;
	

	public ArtistController(ArtistService artistService) {
		this.artistService = artistService;
	}

	@GetMapping(ArtistConstants.GET_ALL_ARTISTS)
	public List<ArtistInfoDTO> getAllArtists() {
		return artistService.getAllArtists();
	}
	
	@GetMapping(ArtistConstants.GET_TOP_ARTISTS)
	public List<ArtistInfoDTO>getTopFiveArtists(){
		return artistService.getTopFiveArtists();
	}
	
	@GetMapping(ArtistConstants.CHECK_FOLLOWING)
	public boolean checkfollowing(@PathVariable Long id, @PathVariable String email) {

		return artistService.CheckFollowing(id, email);
	}
	
	@GetMapping(ArtistConstants.GET_ARTIST_IMAGE)
	public ResponseEntity<byte[]> getArtistImageById(@PathVariable Long id) {

			ArtistDTO artistDTO = artistService.getArtistById(id);
			if (artistDTO == null) {
				return ResponseEntity.notFound().build();
			}

			byte[] imageData = artistDTO.getProfileImage();
			String currentETag = ETagUtil.calculateETag(imageData);

            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                .eTag(currentETag)
                .header(HttpHeaders.CONTENT_TYPE, SongConstants.CONTENT_TYPE_IMAGE)
                .body(imageData);
		
	}
	
	@GetMapping(ArtistConstants.GET_ARTIST_BY_ID)
	public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
		ArtistDTO artistDTO = artistService.getArtistById(id);
		return new ResponseEntity<>(artistDTO, HttpStatus.OK);
	}

	@GetMapping(ArtistConstants.GET_ARTIST_NAME_BY_ID)
	public ResponseEntity<String> getArtistNameId(@PathVariable Long id) {
		ArtistDTO artistDTO = artistService.getArtistById(id);
		return new ResponseEntity<>(artistDTO.getName(), HttpStatus.OK);
	}

	@DeleteMapping(ArtistConstants.DELETE_ARTIST)
	public ResponseEntity<String> deleteArtist(@PathVariable Long id) {
		artistService.deleteArtist(id);
		return new ResponseEntity<>(ArtistConstants.ARTIST_DELETED_SUCCESS.getMessage(), HttpStatus.OK);
	}

	@PutMapping(path = ArtistConstants.FOLLOW_ARTIST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<ArtistDTO> followArtists(@RequestBody ArtistDTO artistDTO,
			@PathVariable String email) {

		ArtistDTO updatedArtistDTO = artistService.followArtists(artistDTO, email);

		return new ResponseEntity<>(updatedArtistDTO, HttpStatus.OK);

	}

}
