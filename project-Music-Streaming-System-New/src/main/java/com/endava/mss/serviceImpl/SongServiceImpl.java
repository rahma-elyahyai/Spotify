package com.endava.mss.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.SongConstants;
import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.entities.Admin;
import com.endava.mss.entities.Album;
import com.endava.mss.entities.Artist;
import com.endava.mss.entities.ETagUtil;
import com.endava.mss.entities.Song;
import com.endava.mss.entities.Song.SongStatus;
import com.endava.mss.entityDTO.ArtistInfoDTO;
import com.endava.mss.entityDTO.NotificationDTO;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.entityDTO.SongInfoDTO;
import com.endava.mss.exception.EmptyFieldException;
import com.endava.mss.exception.Exceptions;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.ArtistMapper;
import com.endava.mss.mapper.SongMapper;
import com.endava.mss.repository.SongRepository;
import com.endava.mss.service.AdminService;
import com.endava.mss.service.AlbumService;
import com.endava.mss.service.ArtistService;
import com.endava.mss.service.NotificationService;
import com.endava.mss.service.SongService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class SongServiceImpl implements SongService {

	private final SongRepository songRepository;
	private final SongMapper songMapper;
	private final ArtistService artistService;
	private final ArtistMapper artistMapper;
	private final AdminService adminService;
	private final AlbumService albumService;
	private final NotificationService notificationService;
	private final EmailNotificationServiceImpl emailNotificationServiceImpl;
	private final Exceptions songExceptions;

	public SongServiceImpl(SongRepository songRepository, SongMapper songMapper, ArtistService artistService,
			ArtistMapper artistMapper, AdminService adminService, AlbumService albumService,
			NotificationService notificationService, EmailNotificationServiceImpl emailNotificationServiceImpl,
			Exceptions songExceptions) {

		this.songRepository = songRepository;
		this.songMapper = songMapper;
		this.artistService = artistService;
		this.artistMapper = artistMapper;
		this.adminService = adminService;
		this.albumService = albumService;
		this.notificationService = notificationService;
		this.emailNotificationServiceImpl = emailNotificationServiceImpl;
		this.songExceptions = songExceptions;
	}

	/**
	 * Retrieves a song by its ID.
	 *
	 * @param id The unique identifier of the song.
	 * @return A ResponseEntity containing the song details if found, or an error
	 *         response if the song is not found.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getSongById(Long id) {
		return songRepository.findById(id)
				.map(song -> ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
						SongConstants.RETURN_SUCCESS_SONG_FETCH.getMessage(), songMapper.SongtoDto(song))))
				.orElseGet(() -> songExceptions.RecordNotFound(SongConstants.ERROR_CODE_SONG_NOT_FOUND.getMessage(),
						SongConstants.ERROR_SONG_NOT_FOUND.getMessage(),
						SongConstants.ERROR_DETAILS_SONG_NOT_FOUND.getMessage(),
						SongConstants.RETURN_ERROR_SONG_NOT_FOUND.getMessage()));
	}

	/**
	 * Retrieves all approved songs.
	 *
	 * @return A ResponseEntity containing a list of approved songs.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getAllApprovedSongs() {
		List<SongInfoDTO> songs = songRepository.getAllApprovedSongs().stream().map(songMapper::songtoSongInfo)
				.collect(Collectors.toList());
		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_SONGS_FETCHED.getMessage(), songs));
	}

	/**
	 * Deletes a song by its ID.
	 *
	 * @param id The unique identifier of the song to be deleted.
	 * @return A ResponseEntity indicating success or failure of the deletion.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> deleteSong(Long id) {
		return songRepository.findById(id).map(song -> {
			songRepository.deleteById(id);
			return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
					SongConstants.RETURN_SUCCESS_SONG_DELETED.getMessage(), null));
		}).orElseGet(() -> songExceptions.RecordNotFound(SongConstants.ERROR_CODE_SONG_NOT_FOUND.getMessage(),
				SongConstants.ERROR_SONG_NOT_FOUND.getMessage(),
				SongConstants.ERROR_DETAILS_SONG_NOT_FOUND.getMessage(),
				SongConstants.RETURN_ERROR_SONG_NOT_FOUND.getMessage()));
	}

	/**
	 * Approves or rejects a song based on the provided action.
	 *
	 * @param id     The unique identifier of the song.
	 * @param action The action to perform ("approved" or "rejected").
	 * @return A ResponseEntity indicating success or failure of the operation.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> approveSongs(Long id, String action) {
		return songRepository.findById(id).map(song -> {
			if ("accepted".equals(action)) {
				song.setStatus(SongStatus.APPROVED);
				notifyArtistAndUsers(song);
			} else {
				song.setStatus(SongStatus.REJECTED);
			}
			Song updatedSong = songRepository.save(song);
			return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
					SongConstants.RETURN_SUCCESS_SONG_UPDATED.getMessage(), songMapper.SongtoDto(updatedSong)));
		}).orElseGet(() -> songExceptions.RecordNotFound(SongConstants.ERROR_CODE_SONG_NOT_FOUND.getMessage(),
				SongConstants.ERROR_SONG_NOT_FOUND.getMessage(),
				SongConstants.ERROR_DETAILS_SONG_NOT_FOUND.getMessage(),
				SongConstants.RETURN_ERROR_SONG_NOT_FOUND.getMessage()));
	}

	/**
	 * Retrieves the top five approved songs based on play count.
	 *
	 * @return A ResponseEntity containing the top five approved songs.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getTopFiveApprovedSongs() {
		List<SongInfoDTO> songs = songRepository.findTopFiveSongs().stream().map(songMapper::songtoSongInfo)
				.collect(Collectors.toList());
		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_TOP_SONGS_FETCHED.getMessage(), songs));
	}

	/**
	 * Increments the play count of a song by 1.
	 *
	 * @param id The unique identifier of the song.
	 * @return A ResponseEntity indicating success or failure of the operation.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> playCount(Long id) {

		Optional<Song> songOptional = songRepository.findById(id);
		if (songOptional.isEmpty()) {
			return songExceptions.RecordNotFound(SongConstants.ERROR_CODE_SONG_NOT_FOUND.getMessage(),
					SongConstants.ERROR_SONG_NOT_FOUND.getMessage(),
					SongConstants.ERROR_DETAILS_SONG_NOT_FOUND.getMessage(),
					SongConstants.RETURN_ERROR_SONG_NOT_FOUND.getMessage());
		}

		Song song = songOptional.get();
		Long presentCount = song.getPlayCount();
		Long newCount = presentCount + 1;

		song.setPlayCount(newCount);
		songRepository.save(song);

		Optional<Artist> artistOptional = artistService.getArtistByIdFromRepo(song.getArtist().getId());
		if (artistOptional.isEmpty()) {
			return songExceptions.RecordNotFound(SongConstants.ERROR_CODE_ARTIST_NOT_FOUND.getMessage(),
					SongConstants.ERROR_ARTIST_NOT_FOUND.getMessage(),
					SongConstants.ERROR_DETAILS_ARTIST_NOT_FOUND.getMessage(),
					SongConstants.RETURN_ERROR_ARTIST_NOT_FOUND.getMessage());
		}

		Artist artist = artistOptional.get();
		Long newArtistSongCount = artist.getTotalPlayCount() + 1;
		artist.setTotalPlayCount(newArtistSongCount);
		artistService.saveArtistInRepo(artist);

		APIGenricResponse response = new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_PLAY_COUNT_UPDATED.getMessage(), songMapper.SongtoDto(song));
		return ResponseEntity.ok(response);
	}

	/**
	 * Retrieves songs filtered by genre and language.
	 *
	 * @param genre    The genre of the songs.
	 * @param language The language of the songs.
	 * @return A ResponseEntity containing the filtered songs.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getFilteredSongs(String genre, String language) {
		List<SongInfoDTO> songs = songRepository.findByGenreAndLanguage(genre, language).stream()
				.map(songMapper::songtoSongInfo).collect(Collectors.toList());
		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_FILTERED_SONGS_FETCHED.getMessage(), songs));
	}

	/**
	 * Retrieves all unique genres from the database.
	 *
	 * @return A ResponseEntity containing a list of all genres.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getAllGenres() {
		List<String> genres = songRepository.findAllGenres();
		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_GENRES_FETCHED.getMessage(), genres));
	}

	/**
	 * Retrieves all unique languages from the database.
	 *
	 * @return A ResponseEntity containing a list of all languages.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getAllLanguages() {
		List<String> languages = songRepository.findAllLanguages();
		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_LANGUAGES_FETCHED.getMessage(), languages));
	}

	/**
	 * Simulates an auto-sleep operation for a specified duration.
	 *
	 * @param duration The duration of the sleep in milliseconds.
	 * @return A ResponseEntity indicating success or failure of the operation.
	 * @throws InterruptedException If the sleep is interrupted.
	 * @throws ExecutionException   If the task execution fails.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> autoSleep(Long duration) throws InterruptedException, ExecutionException {
		Timer timer = new Timer();
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		TimerTask sleepTask = new TimerTask() {

			@Override
			public void run() {
				System.out.println("Autosleep activated.");
				future.complete(true);
			}
		};

		timer.schedule(sleepTask, duration);

		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_AUTO_SLEEP_COMPLETED.getMessage(), future.get()));
	}

	/**
	 * Searches for songs and artists based on a search term.
	 *
	 * @param term The search term.
	 * @param page The page number for paginated results.
	 * @param size The size of the page (number of results per page).
	 * @return A ResponseEntity containing the search results.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> searchSongsAndArtists(String term, int page, int size) {
		if (term == null || term.trim().isEmpty()) {
			return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
					SongConstants.RETURN_SUCCESS_SEARCH_RESULTS_FETCHED.getMessage(),
					Map.of("artist", List.of(), "song", List.of())));
		}

		Pageable pageable = PageRequest.of(page, size);
		Page<Artist> artistPage = artistService.findNameByIgnoringCase(term, pageable);
		Page<Song> songPage = songRepository.findByTitleContainingIgnoreCase(term, pageable);

		List<ArtistInfoDTO> artists = artistPage.getContent().stream()
				.filter(artist -> artist.getSongs() != null && !artist.getSongs().isEmpty())
				.map(artistMapper::artistToArtistInfo).collect(Collectors.toList());


		List<SongInfoDTO> songs = songPage.getContent().stream().map(songMapper::songtoSongInfo)
				.filter(dto -> SongStatus.APPROVED.equals(dto.status())).collect(Collectors.toList());
  
		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_SEARCH_RESULTS_FETCHED.getMessage(),
				Map.of("artist", artists, "song", songs)));
	}

	/**
	 * Performs a voice search for songs based on a search term.
	 *
	 * @param term The search term.
	 * @return A ResponseEntity containing the best matching song.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> voiceSearch(String term) {
		@SuppressWarnings("unchecked")
		Map<String, Object> response = (Map<String, Object>) searchSongsAndArtists(term, 0, 2).getBody().getBody();
		@SuppressWarnings("unchecked")
		List<SongInfoDTO> songs = (List<SongInfoDTO>) response.get("song");

		if (songs == null || songs.isEmpty()) {
			return songExceptions.RecordNotFound(SongConstants.ERROR_CODE_SONG_NOT_FOUND.getMessage(),
					SongConstants.ERROR_SONG_NOT_FOUND.getMessage(),
					SongConstants.ERROR_DETAILS_SONG_NOT_FOUND.getMessage(),
					SongConstants.RETURN_ERROR_SONG_NOT_FOUND.getMessage());
		}

		return ResponseEntity.ok(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_VOICE_SEARCH_RESULT_FETCHED.getMessage(), songs.get(0)));
	}

	/**
	 * Edits an existing song.
	 *
	 * @param id      The unique identifier of the song to be edited.
	 * @param songDTO The SongDTO object containing the updated song details.
	 * @return A ResponseEntity indicating success or failure of the operation.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> editSong(Long id, SongDTO songDTO) {

		Optional<Song> songOptional = songRepository.findById(id);
		if (songOptional.isEmpty()) {
			return songExceptions.RecordNotFound(SongConstants.ERROR_CODE_SONG_NOT_FOUND.getMessage(),
					SongConstants.ERROR_SONG_NOT_FOUND.getMessage(),
					SongConstants.ERROR_DETAILS_SONG_NOT_FOUND.getMessage(),
					SongConstants.RETURN_ERROR_SONG_NOT_FOUND.getMessage());
		}

		Song song = songOptional.get();
		Optional.ofNullable(songDTO.getTitle()).ifPresent(song::setTitle);
		Optional.ofNullable(songDTO.getGenre()).ifPresent(song::setGenre);
		Optional.ofNullable(songDTO.getLanguage()).ifPresent(song::setLanguage);
		Optional.ofNullable(songDTO.getLyrics()).ifPresent(song::setLyrics);
		Optional.ofNullable(songDTO.getReleaseDate()).ifPresent(song::setReleaseDate);
		Optional.ofNullable(songDTO.getCoverImage()).ifPresent(song::setCoverImage);
		Optional.ofNullable(songDTO.getMp3File()).ifPresent(song::setMp3File);

		song.setStatus(SongStatus.PENDING);

		Song updatedSong = songRepository.save(song);

		APIGenricResponse response = new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_SONG_UPDATED.getMessage(), songMapper.SongtoDto(updatedSong));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Notifies the artist and users about a new song.
	 *
	 * @param song The song for which notifications are sent.
	 */
	private void notifyArtistAndUsers(Song song) {
		Artist artist = song.getArtist();
		if (artist != null) {
			String htmlBody = String.format(SongConstants.EMAIL_BODY_NEW_SONG, artist.getName(), song.getTitle());
			String message = String.format(SongConstants.NOTIFICATION_MESSAGE_NEW_SONG, artist.getName(),
					song.getTitle());

			artist.getUsers().forEach(user -> {
				try {
					emailNotificationServiceImpl.sendEmail(user.getAccount().getEmail(),
							SongConstants.EMAIL_SUBJECT_NEW_SONG, htmlBody);
					notificationService.createNewNotification(
							NotificationDTO.builder().message(message).userId(user.getId()).build());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	/**
	 * Retrieves all songs from the database.
	 *
	 * @return A list of all songs.
	 */
	@Override
	public List<SongDTO> findAllSongs() {

		return songRepository.findAll().stream().map(p -> songMapper.SongtoDto(p)).toList();
	}

	
	/**
	 * Streams a song by its ID. Supports partial content for audio streaming.
	 *
	 * @param id      The unique identifier of the song.
	 * @param request The HTTP request containing the range header for partial
	 *                content.
	 * @return A ResponseEntity containing the audio data or error if the song is
	 *         not found.
	 */
	@Override
	public ResponseEntity<byte[]> streamSong(Long id, HttpServletRequest request) {

		Optional<Song> songOptional = songRepository.findById(id);
		Song song = songOptional.get();
		if (song == null || song.getMp3File() == null) {
			return ResponseEntity.notFound().build();
		}
		byte[] audioData = song.getMp3File();
		String rangeHeader = request.getHeader("Range");
		if (rangeHeader == null) {
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("audio/mp3")).body(audioData);
		}
		long fileSize = audioData.length;
		long rangeStart = 0;
		long rangeEnd = fileSize - 1;
		String[] ranges = rangeHeader.replace("bytes=", "").split("-");
		rangeStart = Long.parseLong(ranges[0]);
		if (ranges.length > 1) {
			rangeEnd = Long.parseLong(ranges[1]);
		}

		byte[] partialData = Arrays.copyOfRange(audioData, (int) rangeStart, (int) rangeEnd + 1);

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaType.parseMediaType(SongConstants.CONTENT_TYPE_AUDIO))
				.header(SongConstants.HEADER_CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize)
				.body(partialData);

	}

	/**
	 * Retrieves the cover image of a song by its ID.
	 *
	 * @param id The unique identifier of the song.
	 * @return A ResponseEntity containing the image data or error if the song is
	 *         not found.
	 */
	@Override
	public ResponseEntity<byte[]> songImage(Long id) {

		SongDTO songDTO = songMapper.SongtoDto(songRepository.findById(id).orElse(null));
		if (songDTO == null) {
			return ResponseEntity.notFound().build();
		}
		byte[] imageData = songDTO.getCoverImage();
		String currentETag = ETagUtil.calculateETag(imageData);

		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS)).eTag(currentETag)
				.header(HttpHeaders.CONTENT_TYPE, SongConstants.CONTENT_TYPE_IMAGE).body(imageData);

	}
	

	/**
	 * Fetches a paginated list of song requests based on the provided status.
	 * 
	 * @param status The status of the song requests to be fetched.
	 * @param page   The page number to be fetched.
	 * @param size   The number of entries per page.
	 * @return ResponseEntity containing an APIGenricResponse with the status message,
	 *         a description, and a paginated list of SongInfoDTO objects.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> songRequests(String status, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		SongStatus songStatus = switch (status) {
		case "APPROVED" -> SongStatus.APPROVED;
		case "REJECTED" -> SongStatus.REJECTED;
		default -> SongStatus.PENDING;
		};

		Page<SongInfoDTO> songRequestsPage = songRepository.findByStatus(songStatus, pageable);

		return new ResponseEntity<>(new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				"Song request page fetched", songRequestsPage), HttpStatus.OK);
	}

	/**
	 * Saves a new song to the database.
	 *
	 * @param songDTO The SongDTO object containing the song details to be saved.
	 * @return A ResponseEntity indicating success or failure of the operation.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> saveSong(SongDTO songDTO) {

		Song song = songMapper.DTOtoSong(songDTO);
		Artist artist = artistService.getArtistByIdFromRepo(songDTO.getArtistId())
				.orElseThrow(() -> new UserNotFoundException(SongConstants.ERROR_ARTIST_NOT_FOUND.getMessage()));

		if (songDTO.getTitle() == null || songDTO.getTitle().isEmpty()) {
			throw new EmptyFieldException(SongConstants.ERROR_TITLE_REQUIRED.getMessage());
		}
		if (songDTO.getGenre() == null || songDTO.getGenre().isEmpty()) {
			throw new EmptyFieldException(SongConstants.ERROR_GENRE_REQUIRED.getMessage());
		}
		if (songDTO.getLanguage() == null || songDTO.getLanguage().isEmpty()) {
			throw new EmptyFieldException(SongConstants.ERROR_LANGUAGE_REQUIRED.getMessage());
		}
		if (songDTO.getReleaseDate() == null) {
			throw new EmptyFieldException(SongConstants.ERROR_RELEASE_DATE_REQUIRED.getMessage());
		}
		if (songDTO.getCoverImage() == null || songDTO.getCoverImage().length == 0) {
			throw new EmptyFieldException(SongConstants.ERROR_COVER_IMAGE_REQUIRED.getMessage());
		}
		if (songDTO.getMp3File() == null || songDTO.getMp3File().length == 0) {
			throw new EmptyFieldException(SongConstants.ERROR_MP3_FILE_REQUIRED.getMessage());
		}

		song.setArtist(artist);
		song.setPlayCount(0l);
		song.setStatus(SongStatus.PENDING);// Initial status is PENDING

		Admin admin = adminService.getAdminFromRepo(1L)
				.orElseThrow(() -> new UserNotFoundException(SongConstants.ERROR_ADMIN_NOT_FOUND.getMessage()));// Default admin
		song.setAdmin(admin);

		Album album = albumService.getAlbumById(songDTO.getAlbumId()).orElse(null);// Save album
		if (album == null) {
			song.setAlbum(null);
		}
		if (songDTO.getAlbumId() != null)
			song.setAlbum(album);

		songRepository.save(song);

		
		APIGenricResponse response = new APIGenricResponse(SongConstants.SUCCESS.getMessage(),
				SongConstants.RETURN_SUCCESS_SONG_CREATED.getMessage(), songMapper.SongtoDto(song));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
