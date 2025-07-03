package com.endava.mss.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.Song;
import com.endava.mss.entities.SongQueue;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.SongQueueDTO;
import com.endava.mss.repository.SongRepository;
import com.endava.mss.repository.UserRepository;

@Component
public class SongQueueMapper {


	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SongRepository songRepository;

    public SongQueueDTO SongQueuetoDTO(SongQueue songQueue) {
        if (songQueue == null) {
            return null;
        }

        
        return SongQueueDTO.builder()
                .id(songQueue.getId())
                .currentPosition(songQueue.getCurrentPosition())
                .userId(songQueue.getUser() != null ? songQueue.getUser().getId() : null)
                .songId(songQueue.getSong() != null ? songQueue.getSong().getId() : null)
                .songTitle(songQueue.getSong().getTitle())
                .build();
    }


    public SongQueue DTOtoSongQueue(SongQueueDTO songQueueDTO) {
        if (songQueueDTO == null) {
            return null;
        }

        SongQueue songQueue = new SongQueue();
        songQueue.setId(songQueueDTO.getId());
        songQueue.setCurrentPosition(songQueueDTO.getCurrentPosition()); 
        
        User user = userRepository.findById(songQueueDTO.getUserId()).orElse(null);
        
        if(user != null)
        {
        	songQueue.setUser(user);
        }
        
        Song song = songRepository.findById(songQueueDTO.getSongId()).orElse(null);
        
        if(song != null)
        {
        	songQueue.setSong(song);
        }
        
        return songQueue;
    }
}
