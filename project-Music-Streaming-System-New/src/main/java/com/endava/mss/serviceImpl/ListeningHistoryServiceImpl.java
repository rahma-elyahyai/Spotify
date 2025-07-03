package com.endava.mss.serviceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.ListeningHistoryConstants;
import com.endava.mss.entities.ListeningHistory;
import com.endava.mss.entities.Song;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.ListeningHistoryDTO;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.entityDTO.SongInfoDTO;
import com.endava.mss.exception.SongNotFoundException;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.ListeningHistoryMapper;
import com.endava.mss.mapper.SongMapper;
import com.endava.mss.repository.ListeningHistoryRepository;
import com.endava.mss.repository.SongRepository;
import com.endava.mss.repository.UserRepository;
import com.endava.mss.service.ListeningHistoryService;

@Service
public class ListeningHistoryServiceImpl implements ListeningHistoryService {

	private final ListeningHistoryRepository listeningHistoryRepository;

	private final ListeningHistoryMapper listeningHistoryMapper;

	private final UserRepository userRepository;

	private final SongRepository songRepository;

	private final SongMapper songMapper;
	
	

	public ListeningHistoryServiceImpl(ListeningHistoryRepository listeningHistoryRepository,
			ListeningHistoryMapper listeningHistoryMapper, UserRepository userRepository, SongRepository songRepository,
			SongMapper songMapper) {
		this.listeningHistoryRepository = listeningHistoryRepository;
		this.listeningHistoryMapper = listeningHistoryMapper;
		this.userRepository = userRepository;
		this.songRepository = songRepository;
		this.songMapper = songMapper;
	}

	/**
	 * Adds a song to the user's listening history.
	 * 
	 * @param userId The ID of the user.
	 * @param songId The ID of the song.
	 * @return ListeningHistoryDTO representing the saved listening history.
	 * @throws UserNotFoundException If the user does not exist.
	 * @throws SongNotFoundException If the song does not exist.
	 */
	@Override
	public ListeningHistoryDTO addSongListeningHistory(ListeningHistoryDTO listeningHistoryDTO) {

		User user = userRepository.findById(listeningHistoryDTO.userId())
				.orElseThrow(() -> new UserNotFoundException(ListeningHistoryConstants.USER_NOT_FOUND));

		Song song = songRepository.findById(listeningHistoryDTO.songId())
				.orElseThrow(() -> new SongNotFoundException(ListeningHistoryConstants.SONG_NOT_FOUND));

		ListeningHistory listeningHistory = new ListeningHistory();

		listeningHistory.setSong(song);
		listeningHistory.setUser(user);
		listeningHistory.setDurationPlayed(listeningHistoryDTO.duration());

		listeningHistoryRepository.save(listeningHistory);

		Long currentListenedDuration = song.getListenedDuration();

		if (currentListenedDuration == null) {
			currentListenedDuration = 0l;
		}

		Long updatedListenedDuration = listeningHistoryDTO.duration() + currentListenedDuration;

		song.setListenedDuration(updatedListenedDuration);

		songRepository.save(song);

		return listeningHistoryMapper.ListeningtoDTO(listeningHistory);
	}

	/**
	 * Retrieves the listening history of a user in reverse(recent song) order.
	 * Ensures no duplicate consecutive songs are returned.
	 *
	 * @param userId The ID of the user.
	 * @return List of SongDTO representing the songs the user has listened to.
	 * @throws UserNotFoundException If the user does not exist.
	 */
	@Override
	public List<SongInfoDTO> fetchListeningHistoryOfUser(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(ListeningHistoryConstants.USER_NOT_FOUND));

		List<ListeningHistoryDTO> listeningHistory = listeningHistoryRepository.getListeningHistoryByUser(user).stream()
				.map(p -> listeningHistoryMapper.ListeningtoDTO(p)).collect(Collectors.toList());

		List<SongInfoDTO> songsListened = new ArrayList<SongInfoDTO>();

		Stack<Long> previous = new Stack<Long>();

		listeningHistory.stream().forEach(p -> {

			if (previous.isEmpty() || !previous.peek().equals(p.songId()))// To ensure no duplicate consecutive songs
																				// are returned
				songsListened.add(songMapper.songtoSongInfo(songRepository.findById(p.songId()).orElse(null)));

			previous.push(p.songId());
		});

		Collections.reverse(songsListened);

	    if (songsListened.size() > 20) {
	        return songsListened.subList(0, 20);
	    } else {
	        return songsListened;
	    }
	}

	/**
	 * Clears the user's listening history.
	 *
	 * @param userId The ID of the user.
	 * @throws UserNotFoundException If the user does not exist.
	 */
	@Override
	public void clearListeningHistory(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(ListeningHistoryConstants.USER_NOT_FOUND));

		listeningHistoryRepository.deleteListeningHistory(userId);

	}

	@Override
	public List<ListeningHistory> fetchHistoryOfUser(User user) {

		return listeningHistoryRepository.getListeningHistoryByUser(user);
	}

}
