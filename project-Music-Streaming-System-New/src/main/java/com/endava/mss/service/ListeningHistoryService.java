package com.endava.mss.service;

import java.util.List;

import com.endava.mss.entities.ListeningHistory;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.ListeningHistoryDTO;
import com.endava.mss.entityDTO.SongInfoDTO;

public interface ListeningHistoryService {

	ListeningHistoryDTO addSongListeningHistory(ListeningHistoryDTO listeningHistoryDTO);
	
	List<SongInfoDTO> fetchListeningHistoryOfUser(Long userId);
	
	void clearListeningHistory(Long userId);
	
	List<ListeningHistory>fetchHistoryOfUser(User user);
	
}
