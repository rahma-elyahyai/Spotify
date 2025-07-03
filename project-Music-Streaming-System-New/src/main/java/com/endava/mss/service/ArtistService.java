package com.endava.mss.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.endava.mss.entities.Artist;
import com.endava.mss.entityDTO.ArtistDTO;
import com.endava.mss.entityDTO.ArtistInfoDTO;

public interface ArtistService {

	public List<ArtistInfoDTO> getAllArtists();
	
	public List<ArtistInfoDTO>getTopFiveArtists();

	public ArtistDTO getArtistById(Long Id);

	public ArtistDTO saveArtist(ArtistDTO artistDTO);

	public void deleteArtist(Long id);

	public ArtistDTO checkLogin(ArtistDTO artistDTO);
	
	public ArtistDTO followArtists(ArtistDTO artistDTO, String userEmail);
	
	public boolean CheckFollowing(Long artistId,String userEmail);
	
	public Page<Artist> findNameByIgnoringCase(String term,Pageable pageable);
	
	public void saveArtistInRepo(Artist artist);
	
	public Optional<Artist> getArtistByIdFromRepo(Long Id);

}
