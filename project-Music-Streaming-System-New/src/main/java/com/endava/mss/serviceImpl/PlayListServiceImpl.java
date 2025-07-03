package com.endava.mss.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.endava.mss.constantFiles.PlaylistConstants;
import com.endava.mss.constantFiles.SongConstants;
import com.endava.mss.entities.PlayList;
import com.endava.mss.entities.Song;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.PlayListDTO;
import com.endava.mss.entityDTO.SongQueueDTO;
import com.endava.mss.exception.PlayListNotFoundException;
import com.endava.mss.exception.SongAlreadyExistsInPlayListException;
import com.endava.mss.exception.SongNotFoundException;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.PlayListMapper;
import com.endava.mss.mapper.SongQueueMapper;
import com.endava.mss.repository.PlayListRepository;
import com.endava.mss.repository.SongQueueRepository;
import com.endava.mss.repository.SongRepository;
import com.endava.mss.repository.UserRepository;
import com.endava.mss.service.PlayListService;

@Service
public class PlayListServiceImpl implements PlayListService {


	private final PlayListRepository playListRepository;

	private final PlayListMapper playListMapper;

	private final SongRepository songRepository;


	private final UserRepository userRepository;


	private final SongQueueImpl songQueueImpl;


	private final SongQueueRepository songQueueRepository;

	private final SongQueueMapper songQueueMapper;

	private final JWTService jwtService;
	
	

	public PlayListServiceImpl(PlayListRepository playListRepository, PlayListMapper playListMapper,
			SongRepository songRepository, UserRepository userRepository, SongQueueImpl songQueueImpl,
			SongQueueRepository songQueueRepository, SongQueueMapper songQueueMapper, JWTService jwtService) {
	
		this.playListRepository = playListRepository;
		this.playListMapper = playListMapper;
		this.songRepository = songRepository;
		this.userRepository = userRepository;
		this.songQueueImpl = songQueueImpl;
		this.songQueueRepository = songQueueRepository;
		this.songQueueMapper = songQueueMapper;
		this.jwtService = jwtService;
	}

	/**
	 * Creates a new playlist for a user.
	 * 
	 * @param playListDTO Data Transfer Object containing playlist details.
	 * @return The created playlist as a DTO.
	 * @throws UserNotFoundException if the user with the provided ID does not
	 *                               exist.
	 */
	@Override
	public PlayListDTO createPlayList(PlayListDTO playListDTO) {

		User user = userRepository.findById(playListDTO.getUserId())
				.orElseThrow(() -> new UserNotFoundException(PlaylistConstants.USER_NOT_FOUND_ERROR_MESSAGE));
		PlayList playList = playListMapper.DTOtoPlayList(playListDTO);
		playList.setUser(user);
		playListRepository.save(playList);
		return playListMapper.PlayListToDTO(playList);
	}

	/**
	 * Adds a song to an existing playlist..
	 * 
	 * @param playListId ID of the playlist.
	 * @param songId     ID of the song to add.
	 * @return The updated playlist as a DTO.
	 * @throws PlayListNotFoundException if the playlist with the given ID does not
	 *                                   exist.
	 * @throws SongNotFoundException     if the song with the given ID does not
	 *                                   exist.
	 */
	@Override
	public PlayListDTO addSongtoPlayList(Long playListId, Long songId) {

		PlayList playList = playListRepository.findById(playListId)
				.orElseThrow(() -> new PlayListNotFoundException(PlaylistConstants.PLAYLIST_NOT_FOUND_ERROR_MESSAGE));
		Song song = songRepository.findById(songId)
				.orElseThrow(() -> new SongNotFoundException(PlaylistConstants.SONG_NOT_FOUND_ERROR_MESSAGE));

		List<Song> songs = playList.getSongs();
		if (songs != null && !songs.contains(song)) {
			songs.add(song);
		} else if (songs != null && songs.contains(song)) {
			throw new SongAlreadyExistsInPlayListException(PlaylistConstants.SONG_ALREADY_EXISTS_ERROR_MESSAGE);
		} else {
			List<Song> newSongs = new ArrayList<Song>();
			newSongs.add(song);
			playList.setSongs(newSongs);

		}

		playListRepository.save(playList);

		return playListMapper.PlayListToDTO(playList);
	}

	/**
	 * Fetches a playlist by its ID.
	 * 
	 * @param playListId ID of the playlist to fetch.
	 * @return The playlist as a DTO.
	 * @throws PlayListNotFoundException if the playlist with the given ID does not
	 *                                   exist.
	 * @return The fetched playlist as a DTO.
	 */
	@Override
	public PlayListDTO fetchPlayList(Long playListId) {

		PlayList playList = playListRepository.findById(playListId)
				.orElseThrow(() -> new PlayListNotFoundException(PlaylistConstants.PLAYLIST_NOT_FOUND_ERROR_MESSAGE));
		return playListMapper.PlayListToDTO(playList);
	}

	/**
	 * Deletes playlist by its ID.
	 * 
	 * @param playListId ID of the playlist to delete.
	 * @throws PlayListNotFoundException if the playlist with the given ID does not
	 *                                   exist.
	 */
	@Transactional
	@Override
	public void deletePlayList(Long playListId) {

		PlayList playList = playListRepository.findById(playListId)
				.orElseThrow(() -> new PlayListNotFoundException(PlaylistConstants.PLAYLIST_NOT_FOUND_ERROR_MESSAGE));
		System.out.println(playListId);
		playListRepository.deleteById(playListId);


	}

	/**
	 * Deletes a song in a playlist
	 *
	 * @param songId     ID of song which has to be deleted.
	 * @param playListId ID of playlist from where song has to deleted
	 * @throws PlayListNotFoundException if the playlist with the given ID does not
	 *                                   exist.
	 * @throws SongNotFoundException     if the song with the given ID does not
	 *                                   exist.
	 */
	@Override
	public void deleteSongFromPlayList(Long playListId, Long songId) {

		PlayList playList = playListRepository.findById(playListId)
				.orElseThrow(() -> new PlayListNotFoundException(PlaylistConstants.PLAYLIST_NOT_FOUND_ERROR_MESSAGE));
		Song song = songRepository.findById(songId)
				.orElseThrow(() -> new SongNotFoundException(SongConstants.ERROR_SONG_NOT_FOUND.getMessage()));

		playListRepository.deleteSongFromPlaylist(playListId, songId);

	}

	/**
	 * Autoplays all songs in the playlist by passing all the songs to the queue.
	 *
	 * @param playListDTO Data Transfer Object containing playlist details.
	 * @return The playlist stored in the queue as a DTO.
	 * @throws PlayListNotFoundException if the playlist with the given ID does not
	 *                                   exist.
	 * @throws SongNotFoundException     if the song with the given ID does not
	 *                                   exist.
	 */
	@Override
	public PlayListDTO playSongsfromPlaylist(PlayListDTO playListDTO) {

		songQueueImpl.clearSongQueues(playListDTO.getUserId());

		PlayList userPlayList = playListRepository.findById(playListDTO.getId())
				.orElseThrow(() -> new PlayListNotFoundException(PlaylistConstants.PLAYLIST_NOT_FOUND_ERROR_MESSAGE));

		List<SongQueueDTO> songQueueDTOList = userPlayList.getSongs().stream()
				.map(s -> SongQueueDTO.builder().userId(playListDTO.getUserId()).songId(s.getId()).build())
				.collect(Collectors.toList());

		for (int i = 0; i < songQueueDTOList.size(); i++) {
			songQueueDTOList.get(i).setCurrentPosition((long) i + 1);
			songQueueRepository.save(songQueueMapper.DTOtoSongQueue(songQueueDTOList.get(i)));

		}
		User user = userRepository.findById(playListDTO.getUserId())
				.orElseThrow(() -> new UserNotFoundException(PlaylistConstants.USER_NOT_FOUND_ERROR_MESSAGE));
		user.setCurrentSongQueueId(2l);

		userRepository.save(user);

		return playListMapper.PlayListToDTO(userPlayList);
	}

	@Override
	public PlayListDTO saveSharedPlaylist(Long playlistId, String token) {

		String storedToken = token;

		String userName = jwtService.extractUserName(storedToken);
		
		User user = userRepository.findByAccount_Email(userName);
		
		PlayList playList= playListRepository.findById(playlistId)
				.orElseThrow(() -> new PlayListNotFoundException(PlaylistConstants.PLAYLIST_NOT_FOUND_ERROR_MESSAGE));

		List<Song> copiedSongs = new ArrayList<>(playList.getSongs());
		  
		PlayList sharedPlayList=PlayList.builder().name(playList.getName()).songs(copiedSongs)
				.user(user).build();
	
		playListRepository.save(sharedPlayList);
		return playListMapper.PlayListToDTO(sharedPlayList);
	}

}
