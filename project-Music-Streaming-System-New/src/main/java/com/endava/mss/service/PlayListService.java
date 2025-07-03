package com.endava.mss.service;

import java.util.List;

import com.endava.mss.entityDTO.PlayListDTO;

public interface PlayListService {

	PlayListDTO createPlayList(PlayListDTO playListDTO);
	
	PlayListDTO addSongtoPlayList(Long playListId,Long SongId);
	
	PlayListDTO fetchPlayList(Long playListId);
	
	void deletePlayList(Long playListId);
	
	void deleteSongFromPlayList(Long playListId,Long SongId);
	
	PlayListDTO playSongsfromPlaylist(PlayListDTO playListDTO);
	
	PlayListDTO saveSharedPlaylist(Long playlistId,String token);
}
