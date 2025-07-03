package com.endava.mss.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.Artist;
import com.endava.mss.entities.LiveConcerts;
import com.endava.mss.entityDTO.LiveConcertsDTO;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.repository.ArtistRepository;

@Component
public class LiveConcertsMapper {

	@Autowired
	private ArtistRepository artistRepository;

    public LiveConcertsDTO liveConcertstoDTO(LiveConcerts liveConcerts) {
        if (liveConcerts == null) {
            return null;
        }
        return LiveConcertsDTO.builder()
                .Id(liveConcerts.getId())
                .date(liveConcerts.getDate())
                .location(liveConcerts.getLocation())
                .artistId(liveConcerts.getArtist() != null ? liveConcerts.getArtist().getId() : null)
                .build();
    }

    public LiveConcerts DTOtoLiveConcerts(LiveConcertsDTO dto) {
        if (dto == null) {
            return null;
        }
       
        Artist artist = artistRepository.findById(dto.getArtistId()).orElse(null);
        
        if(artist==null)
        {
        	throw new UserNotFoundException("artist not found");
        }
        
        LiveConcerts liveConcerts = LiveConcerts.builder()
                .Id(dto.getId())
                .date(dto.getDate())
                .location(dto.getLocation())
                .artist(artist)
                .build();

        return liveConcerts;
    }
}
