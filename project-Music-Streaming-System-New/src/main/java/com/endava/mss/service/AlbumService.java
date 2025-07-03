package com.endava.mss.service;

import java.util.List;
import java.util.Optional;

import com.endava.mss.entities.Album;
import com.endava.mss.entityDTO.AlbumDTO;
import com.endava.mss.entityDTO.AlbumInfoDTO;


public interface AlbumService {


	public List<AlbumInfoDTO> getAlbumsByArtistId(Long Id);

	public AlbumDTO createAlbum(AlbumDTO albumDTO);
	
	public void deleteAlbum(Long Id);
	
	public Optional<Album> getAlbumById(Long Id);
}
