package com.endava.mss.service;

import java.util.List;

import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.entityDTO.SongQueueDTO;

public interface SongQueueService {

	SongQueueDTO addSongtoQueue(SongQueueDTO songQueueDTO);
	
	List<SongQueueDTO> getAllSongQueues(Long userId);
	
	void clearSongQueues(Long userId);
	
	List<SongQueueDTO> reorderSongQueue(Long userId, List<SongQueueDTO>currentQueue);
	
	SongDTO getNextSongFromSongQueue(Long userId);
	
    SongDTO getPreviousSongFromSongQueue(Long userId);
    
    List<SongQueueDTO>shuffleSongs(Long userId);
    
    void deleteSongInSongQueue(Long songQueueId);
	
}
