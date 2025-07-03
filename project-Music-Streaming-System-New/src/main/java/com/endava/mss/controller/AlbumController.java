package com.endava.mss.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.endava.mss.constantFiles.AlbumConstants;
import com.endava.mss.entities.ETagUtil;
import com.endava.mss.entityDTO.AlbumDTO;
import com.endava.mss.mapper.AlbumMapper;
import com.endava.mss.repository.AlbumRepository;
import com.endava.mss.service.AlbumService;

@RestController
@RequestMapping(AlbumConstants.ALBUM_PATH)
public class AlbumController {

	private final AlbumService albumService;

	private final AlbumRepository albumRepository;

	private final AlbumMapper albumMapper;

	public AlbumController(AlbumService albumService, AlbumRepository albumRepository, AlbumMapper albumMapper) {

		this.albumService = albumService;
		this.albumRepository = albumRepository;
		this.albumMapper = albumMapper;
	}

	@GetMapping(AlbumConstants.ALBUM_IMAGE_PATH)
	public ResponseEntity<byte[]> getAlbumImageById(@PathVariable Long id) {
		AlbumDTO albumDTO = albumMapper.albumTAlbumDTO(albumRepository.findById(id).orElse(null));
		byte[] imageData = albumDTO.getCoverImage();
		String currentETag = ETagUtil.calculateETag(imageData);

		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS)).eTag(currentETag)
				.header("Content-Type", AlbumConstants.CONTENT_TYPE_IMAGE_JPEG).body(albumDTO.getCoverImage());

	}

	@PostMapping
	public AlbumDTO createAlbum(@RequestParam String title, @RequestParam LocalDate releaseDate,
			@RequestParam MultipartFile coverImage, @RequestParam Long artistId) {

		AlbumDTO albumDTO = AlbumDTO.builder().title(title).releaseDate(releaseDate).artistId(artistId).build();

		if (coverImage != null && !coverImage.isEmpty()) {

			byte[] coverImageBytes = convertFileToByteArray(coverImage);
			albumDTO.setCoverImage(coverImageBytes);
		}

		albumService.createAlbum(albumDTO);

		return albumDTO;
	}

	private byte[] convertFileToByteArray(MultipartFile file) {
		try {
			return file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	@DeleteMapping(AlbumConstants.ALBUM_DELETE_PATH)
	public ResponseEntity<String> deleteArtist(@PathVariable Long id) {
		albumService.deleteAlbum(id);
		return new ResponseEntity<>(AlbumConstants.ALBUM_DELETION_SUCCESS, HttpStatus.OK);
	}
}
