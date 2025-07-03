package com.endava.mss.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.PlayList;
import com.endava.mss.entityDTO.PlayListDTO;
import com.endava.mss.entityDTO.SongInfoDTO;

@Component
public class PlayListMapper {

	@Autowired
	SongMapper songMapper;
	
    public PlayListDTO PlayListToDTO(PlayList playList) {
        if (playList == null) {
            return null;
        }
        
        
        PlayListDTO playListDTO= PlayListDTO.builder()
                .id(playList.getId())
                .playListName(playList.getName())
                .userId(playList.getUser().getId()) 
                .userName(playList.getUser().getName())
                .build();
        
        if(playList.getSongs()!=null)
        {
        List<SongInfoDTO> songDTOs = playList.getSongs().stream()
                .map(p->songMapper.songtoSongInfo(p))
                .collect(Collectors.toList());
        
        playListDTO.setSongs(songDTOs);
        }
    
       
        
        return playListDTO;
    }

   
    public PlayList DTOtoPlayList(PlayListDTO playListDTO) {
        if (playListDTO == null) {
            return null;
        }
        
        return PlayList.builder()
                .id(playListDTO.getId())
                .name(playListDTO.getPlayListName())
                .build();
    }
}
