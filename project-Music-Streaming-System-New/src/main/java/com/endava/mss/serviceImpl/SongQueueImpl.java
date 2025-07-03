package com.endava.mss.serviceImpl;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.SongQueueConstants;
import com.endava.mss.entities.Song;
import com.endava.mss.entities.SongQueue;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.entityDTO.SongQueueDTO;
import com.endava.mss.exception.EmptyQueueException;
import com.endava.mss.exception.SongNotFoundException;
import com.endava.mss.exception.SongQueueNotFoundException;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.SongMapper;
import com.endava.mss.mapper.SongQueueMapper;
import com.endava.mss.repository.SongQueueRepository;
import com.endava.mss.repository.SongRepository;
import com.endava.mss.repository.UserRepository;
import com.endava.mss.service.SongQueueService;

@Service
public class SongQueueImpl implements SongQueueService {


	private final SongQueueRepository songQueueRepository;

	private final SongQueueMapper songQueueMapper;


	private final UserRepository userRepository;

	private final SongRepository songRepository;

	private final SongMapper songMapper;
	
	

	public SongQueueImpl(SongQueueRepository songQueueRepository, SongQueueMapper songQueueMapper,
			UserRepository userRepository, SongRepository songRepository, SongMapper songMapper) {

		this.songQueueRepository = songQueueRepository;
		this.songQueueMapper = songQueueMapper;
		this.userRepository = userRepository;
		this.songRepository = songRepository;
		this.songMapper = songMapper;
	}

	/**
	 * Adds a song to the user's queue.
	 * 
	 * @param songQueueDTO The song queue DTO containing user ID and song details.
	 * @return SongQueueDTO representing the added song in the queue.
	 * @throws UserNotFoundException if the user is not found.
	 */
	@Override
	public SongQueueDTO addSongtoQueue(SongQueueDTO songQueueDTO) {

		User user = userRepository.findById(songQueueDTO.getUserId())
				.orElseThrow(() -> new UserNotFoundException(SongQueueConstants.USER_NOT_FOUND_ERROR_MESSAGE));

		if (user.getSongQueues() == null) {
			songQueueDTO.setCurrentPosition(1l);
		} else {
			Long currentSongQueueLength = (long) user.getSongQueues().size();

			songQueueDTO.setCurrentPosition(currentSongQueueLength + 1);
		}
		SongQueue songQueue = songQueueMapper.DTOtoSongQueue(songQueueDTO);

		if (songQueueRepository.existsSongInUserQueue(user.getId(), songQueueDTO.getSongId()) == 0)
			songQueueRepository.save(songQueue);

		return songQueueMapper.SongQueuetoDTO(songQueue);
	}

	/**
	 * Retrieves all songs in a user's queue.
	 * 
	 * @param userId The ID of the user.
	 * @return List of SongQueueDTO representing the user's song queue.
	 * @throws UserNotFoundException if the user is not found.
	 */
	@Override
	public List<SongQueueDTO> getAllSongQueues(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SongQueueConstants.USER_NOT_FOUND_ERROR_MESSAGE));

		List<SongQueueDTO> userSongQueue = songQueueRepository.getSongQueueByUser(user).stream()
				.map(p -> songQueueMapper.SongQueuetoDTO(p)).collect(Collectors.toList());

		return userSongQueue;
	}

	/**
	 * Clears all songs from a user's queue.
	 * 
	 * @param userId The ID of the user.
	 * @throws UserNotFoundException if the user is not found.
	 */
	@Override
	public void clearSongQueues(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SongQueueConstants.USER_NOT_FOUND_ERROR_MESSAGE));

		songQueueRepository.clearUserSongQueue(userId);
		user.setCurrentSongQueueId(1l);
		userRepository.save(user);

	}

	/**
	 * Reorders the user's song queue based on a given order.
	 * 
	 * @param userId       The ID of the user.
	 * @param currentQueue List of SongQueueDTO representing the reordered queue.
	 * @return List of SongQueueDTO in the new order.
	 * @throws UserNotFoundException      if the user is not found.
	 * @throws SongQueueNotFoundException if a song queue entry is not found.
	 */
	@Override
	public List<SongQueueDTO> reorderSongQueue(Long userId, List<SongQueueDTO> currentQueue) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SongQueueConstants.USER_NOT_FOUND_ERROR_MESSAGE));
		for (int i = 0; i < currentQueue.size(); i++) {
			SongQueueDTO songQueueDTO = currentQueue.get(i);
			SongQueue songQueue = songQueueRepository.findById(songQueueDTO.getId()).orElseThrow(
					() -> new SongQueueNotFoundException(SongQueueConstants.SONG_QUEUE_NOT_FOUND_ERROR_MESSAGE));
			songQueue.setCurrentPosition((long) (i + 1));
			songQueueRepository.save(songQueue);
		}

		user.setCurrentSongQueueId(1l);
		userRepository.save(user);
		return currentQueue;
	}

	/**
	 * Retrieves the next song in the queue and updates the user's current queue
	 * position.
	 * 
	 * @param userId The ID of the user.
	 * @return SongDTO representing the next song.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws EmptyQueueException   if no next song is available.
	 * @throws SongNotFoundException if the song does not exist.
	 */
	@Override
	public SongDTO getNextSongFromSongQueue(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SongQueueConstants.USER_NOT_FOUND_ERROR_MESSAGE));
		Long songId = songQueueRepository.getSongFromCurrentPosition(userId, user.getCurrentSongQueueId());

		System.out.println(songId);
		if (songId == null) {

			System.out.println("exception");
			if (user.getCurrentSongQueueId() - 1 <= user.getSongQueues().size()
					&& user.getCurrentSongQueueId() - 1 >= 1) {
				user.setCurrentSongQueueId(user.getCurrentSongQueueId() - 1);
				userRepository.save(user);
			}
			throw new EmptyQueueException(SongQueueConstants.EMPTY_QUEUE_NO_NEXT_SONG);
		}

		Song song = songRepository.findById(songId)
				.orElseThrow(() -> new SongNotFoundException(SongQueueConstants.SONG_NOT_FOUND_ERROR_MESSAGE));

		user.setCurrentSongQueueId(user.getCurrentSongQueueId() + 1);
		userRepository.save(user);

		return songMapper.SongtoDto(song);
	}

	/**
	 * Retrieves the previous song in the queue and updates the user's current queue
	 * position.
	 * 
	 * @param userId The ID of the user.
	 * @return SongDTO representing the previous song.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws EmptyQueueException   if no previous song is available.
	 * @throws SongNotFoundException if the song does not exist.
	 */
	@Override
	public SongDTO getPreviousSongFromSongQueue(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SongQueueConstants.USER_NOT_FOUND_ERROR_MESSAGE));

		if (user.getCurrentSongQueueId() == null || user.getCurrentSongQueueId() <= 1) {

			if (user.getCurrentSongQueueId() + 1 <= user.getSongQueues().size()) {
				user.setCurrentSongQueueId(user.getCurrentSongQueueId() + 1);
				userRepository.save(user);
			}
			throw new EmptyQueueException(SongQueueConstants.EMPTY_QUEUE_NO_PREVIOUS_SONG);
		}

		Long previousSongId = songQueueRepository.getSongFromCurrentPosition(userId, user.getCurrentSongQueueId() - 1);

		if (previousSongId == null) {

			if (user.getCurrentSongQueueId() - 1 <= user.getSongQueues().size()
					&& user.getCurrentSongQueueId() - 1 >= 1) {
				user.setCurrentSongQueueId(user.getCurrentSongQueueId() - 1);
				userRepository.save(user);
			}
			throw new EmptyQueueException(SongQueueConstants.EMPTY_QUEUE_NO_PREVIOUS_SONG);
		}

		Song song = songRepository.findById(previousSongId)
				.orElseThrow(() -> new SongNotFoundException(SongQueueConstants.SONG_NOT_FOUND_ERROR_MESSAGE));

		user.setCurrentSongQueueId(user.getCurrentSongQueueId() - 1);
		userRepository.save(user);

		return songMapper.SongtoDto(song);
	}

	/**
	 * Shuffles the user's song queue and updates their order.
	 * 
	 * @param userId The ID of the user.
	 * @return List of shuffled SongQueueDTO.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws EmptyQueueException   if the queue is empty.
	 */
	@Override
	public List<SongQueueDTO> shuffleSongs(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SongQueueConstants.USER_NOT_FOUND_ERROR_MESSAGE));

		List<SongQueueDTO> userSongQueues = user.getSongQueues().stream().map(p -> songQueueMapper.SongQueuetoDTO(p))
				.collect(Collectors.toList());

		if (userSongQueues == null || userSongQueues.isEmpty()) {
			throw new EmptyQueueException(SongQueueConstants.EMPTY_QUEUE_ERROR_MESSAGE);
		}

		Collections.shuffle(userSongQueues);

		for (int i = 0; i < userSongQueues.size(); i++) {
			userSongQueues.get(i).setCurrentPosition((long) i + 1);
		}

		user.setSongQueues(
				userSongQueues.stream().map(p -> songQueueMapper.DTOtoSongQueue(p)).collect(Collectors.toList()));
		user.setCurrentSongQueueId(1l);
		userRepository.save(user);

		return user.getSongQueues().stream().map(p -> songQueueMapper.SongQueuetoDTO(p)).collect(Collectors.toList());
	}

	/**
	 * Deletes a song from the queue by its ID.
	 * 
	 * @param songQueueId The ID of the song in the queue.
	 * @throws SongQueueNotFoundException if the song queue entry is not found.
	 */
	@Override
	public void deleteSongInSongQueue(Long songQueueId) {

		SongQueue songQueue = songQueueRepository.findById(songQueueId).orElseThrow(
				() -> new SongQueueNotFoundException(SongQueueConstants.SONG_QUEUE_NOT_FOUND_ERROR_MESSAGE));

		User user = songQueue.getUser();
		songQueueRepository.delete(songQueue);

	
		List<SongQueue> remainingSongs = songQueueRepository.getSongQueueByUser(user);
		for (int i = 0; i < remainingSongs.size(); i++) {
			remainingSongs.get(i).setCurrentPosition((long) (i + 1));
			songQueueRepository.save(remainingSongs.get(i));
		}

		if (user.getCurrentSongQueueId() > remainingSongs.size()) {
			user.setCurrentSongQueueId((long) remainingSongs.size());
			userRepository.save(user);

		}
	}

}
