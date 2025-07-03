package com.endava.mss.serviceImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.endava.mss.entities.APIGenricResponse;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.AnalyticsReportDTO;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.exception.ErrorResponse;
import com.endava.mss.mapper.ArtistMapper;
import com.endava.mss.mapper.SongMapper;
import com.endava.mss.service.AnalyticsReportService;
import com.endava.mss.service.ArtistService;
import com.endava.mss.service.ListeningHistoryService;
import com.endava.mss.service.SongService;
import com.endava.mss.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class AnalyticsReportServiceImpl implements AnalyticsReportService {

	private static final Logger logger = LoggerFactory.getLogger(AnalyticsReportServiceImpl.class);

	private final SongService songService;

	private final UserService userService;

	private final ArtistService artistService;

	private final ArtistMapper artistMapper;
	
	private final SongMapper songMapper;

	private final ListeningHistoryService listeningHistoryService;
	
	private List<SongDTO>songs;

	public AnalyticsReportServiceImpl(SongService songService, UserService userService, ArtistService artistService,
			ListeningHistoryService listeningHistoryService, ArtistMapper artistMapper,SongMapper songMapper) {

		this.songService = songService;
		this.userService = userService;
		this.artistService = artistService;
		this.listeningHistoryService = listeningHistoryService;
		this.artistMapper = artistMapper;
		this.songMapper = songMapper;
		songs= songService.findAllSongs();
	}

	@Override
	@Transactional 
	public ResponseEntity<APIGenricResponse> getAnalytics() {
	
		CompletableFuture<Map<String, Long>> future1 = CompletableFuture.supplyAsync(() -> getGenrePopularity());
		CompletableFuture<Map<String, Integer>> future2 = CompletableFuture
				.supplyAsync(() -> getSongVerificationStatus());
		CompletableFuture<Map<String, Long>> future3 = CompletableFuture.supplyAsync(() -> getMostPlayedSongs());
		CompletableFuture<TreeMap<String, Integer>> future4 = CompletableFuture.supplyAsync(() -> getUserGrowth());
		CompletableFuture<Map<String, Integer>> future5 = CompletableFuture.supplyAsync(() -> getReleaseTrends());
		CompletableFuture<Map<String, Long>> future6 = CompletableFuture.supplyAsync(() -> getListeningDurations());
		
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1,future2,future3,future4,future5,
				future6);

		APIGenricResponse response;
		try {
			response = new APIGenricResponse("Success", "fetched all analytics for admin",
					AnalyticsReportDTO.builder().genrePopularity(future1.get()).songVerification(future2.get())
							.userGrowth(future4.get()).releaseTrends(future5.get()).playCounts(future3.get())
							.listenedDurations(future6.get()).build());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new APIGenricResponse("Error", "Get analytics failed",
				new ErrorResponse("ANALYTICS_ERROR", "analytics failed", "fail")),HttpStatus.BAD_REQUEST);
	}

	/**
	 * Calculates the popularity of each genre based on the total play count of
	 * songs in that genre.
	 * 
	 * @return A Map where the key is the genre and the value is the total play
	 *         count for that genre.
	 */
	@Override
	public Map<String, Long> getGenrePopularity() {
		System.out.println("1");
		logger.info("Executing getGenrePopularity on thread: {}", Thread.currentThread().getName());
		return songs.stream().collect(Collectors.groupingBy(song -> song.getGenre().toLowerCase(),
				Collectors.summingLong(song -> song.getPlayCount() != null ? song.getPlayCount() : 0)));
	}

	/**
	 * Retrieves the most played songs, filtering only those with an "APPROVED"
	 * status.
	 * 
	 * @return A Map where the key is the song title and the value is the total play
	 *         count of that song.
	 */
	@Override
	public Map<String, Long> getMostPlayedSongs() {
		System.out.println("2");
		logger.info("Executing getMostPlayedSongs on thread: {}", Thread.currentThread().getName());
		return songs.stream().filter(song -> "APPROVED".equals(song.getStatus().toString()))
				.collect(Collectors.groupingBy(song -> song.getTitle(),
						Collectors.summingLong(song -> song.getPlayCount() != null ? song.getPlayCount() : 0)));
	}

	/**
	 * Retrieves the verification status of songs (approved, pending, rejected) and
	 * counts how many songs exist for each status.
	 * 
	 * @return A Map where the key is the status of the song and the value is the
	 *         count of songs in that status.
	 */
	@Override
	public Map<String, Integer> getSongVerificationStatus() {
		System.out.println("3");
		logger.info("Executing getSongVerificationStatus on thread: {}", Thread.currentThread().getName());
		return songs.stream()
				.collect(Collectors.groupingBy(song -> song.getStatus().toString(), Collectors.summingInt(song -> 1)));
	}

	/**
	 * Retrieves user growth statistics based on the registration date, grouped by
	 * month and year.
	 * 
	 * @return A TreeMap of user growth where the key is the month-year format and
	 *         the value is the number of users.
	 */
	@Override
	public TreeMap<String, Integer> getUserGrowth() {
		System.out.println("4");
		logger.info("Executing getUserGrowth on thread: {}", Thread.currentThread().getName());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

		Map<String, Integer> userGrowth = userService.getAllUsers().stream().collect(
				Collectors.groupingBy(user -> user.getCreatedAt().format(formatter), Collectors.summingInt(user -> 1)));

		return new TreeMap<>(userGrowth);
	}

	/**
	 * Retrieves release trends based on song release dates, grouped by month and
	 * year.
	 * 
	 * @return A Map where the key is the month-year format and the value is the
	 *         number of songs released in that month.
	 */
	@Override
	public Map<String, Integer> getReleaseTrends() {
		System.out.println("5");
		logger.info("Executing getReleaseTrends on thread: {}", Thread.currentThread().getName());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

		return songs.stream().collect(Collectors
				.groupingBy(song -> song.getReleaseDate().format(formatter), Collectors.summingInt(song -> 1)));
	}

	/**
	 * Retrieves the total listening durations of each song, in minutes, filtering
	 * only "APPROVED" songs.
	 * 
	 * @return A Map where the key is the song title and the value is the total
	 *         listened duration of that song.
	 */
	@Override
	public Map<String, Long> getListeningDurations() {
		System.out.println("6");
		logger.info("Executing getListening on thread: {}", Thread.currentThread().getName());
		return songs.stream().filter(song -> "APPROVED".equals(song.getStatus().toString()))
				.collect(Collectors.groupingBy(song -> song.getTitle(), Collectors.summingLong(
						song -> song.getListenedDuration() != null ? song.getListenedDuration() / 60 : 0)));
	}

	/**
	 * Retrieves artist-specific analytics based on play counts for songs belonging
	 * to the artist.
	 * 
	 * @param artistId The ID of the artist whose analytics are being fetched.
	 * @return A Map where the key is the song title and the value is the total play
	 *         count for that song by the artist.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getArtistAnalytics(Long artistId) {
		APIGenricResponse response = new APIGenricResponse("Success", "Fetched artist analytics of a particular artist",
				Optional.ofNullable(artistMapper.DtoToArtist(artistService.getArtistById(artistId)))
						.map(artist -> artist.getSongs().stream()
								.collect(Collectors.groupingBy(song -> song.getTitle(),
										Collectors.summingLong(
												song -> song.getPlayCount() != null ? song.getPlayCount() : 0))))
						.orElse(Map.of()));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Retrieves play count statistics for songs listened to by a specific user.
	 * 
	 * @param userId The ID of the user whose song listening history is being
	 *               analyzed.
	 * @return A Map where the key is the song title and the value is the number of
	 *         times that song has been listened to by the user.
	 */
	@Override
	public ResponseEntity<APIGenricResponse> getPlayCountsBySong(Long userId) {

		User user = userService.findUserById(userId);
		APIGenricResponse response = new APIGenricResponse("Success", "Fetched user anlaytics of a particular user",
				listeningHistoryService.fetchHistoryOfUser(user).stream().collect(
						Collectors.groupingBy(history -> history.getSong().getTitle(), Collectors.counting())));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
