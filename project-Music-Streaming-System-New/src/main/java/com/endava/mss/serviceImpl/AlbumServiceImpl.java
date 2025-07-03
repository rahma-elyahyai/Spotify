package com.endava.mss.serviceImpl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.AlbumConstants;
import com.endava.mss.entities.Album;
import com.endava.mss.entities.Artist;
import com.endava.mss.entityDTO.AlbumDTO;
import com.endava.mss.entityDTO.AlbumInfoDTO;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.AlbumMapper;
import com.endava.mss.mapper.ArtistMapper;
import com.endava.mss.repository.AlbumRepository;
import com.endava.mss.repository.ArtistRepository;
import com.endava.mss.service.AlbumService;
import com.endava.mss.service.ArtistService;

@Service
public class AlbumServiceImpl implements AlbumService {

	private final AlbumRepository albumRepository;


	private final AlbumMapper albumMapper;

	private final ArtistService artistService;
	
	private final ArtistMapper artistMapper;
	

	public AlbumServiceImpl(AlbumRepository albumRepository, AlbumMapper albumMapper, ArtistService artistService,
			ArtistMapper artistMapper) {

		this.albumRepository = albumRepository;
		this.albumMapper = albumMapper;
		this.artistService = artistService;
		this.artistMapper = artistMapper;
	}

	/**
	 * @param Id the ID of the artist whose albums are to be fetched
	 * @return List of AlbumDTO objects associated with the specified artist ID
	 * @throws UserNotFoundException if the artist with the given ID does not exist
	 * 
	 *                               This method retrieves the ablums belonging to
	 *                               particular artist based on artist's ID.
	 */
	@Override
	public List<AlbumInfoDTO> getAlbumsByArtistId(Long Id) {

		Artist artist = artistMapper.DtoToArtist(artistService.getArtistById(Id));
		List<AlbumInfoDTO> albumDTO = artist.getAlbums().stream().map(p -> albumMapper.albumToAlbumInfoDTO(p)).toList();
		return albumDTO;
	}

	/**
	 * @param albumDTO the AlbumDTO object containing the details of the album to be
	 *                 created
	 * @return the created AlbumDTO object after saving the album
	 * 
	 *         This method takes an AlbumDTO, converts it to an Album entity, and
	 *         saves it to the repository.
	 */
	@Override
	public AlbumDTO createAlbum(AlbumDTO albumDTO) {

		Album album = albumMapper.AlbumDTOtoAlbum(albumDTO);
		albumRepository.save(album);
		return albumMapper.albumTAlbumDTO(album);
	}

	/**
	 * @param Id the ID of the album to be deleted
	 * 
	 *           This method deletes an album from the repository based on the
	 *           provided ID.
	 */
	@Override
	public void deleteAlbum(Long Id) {

		albumRepository.deleteById(Id);
	}

	@Override
	public Optional<Album> getAlbumById(Long Id) {
		
		return albumRepository.findById(Id);
	}
}
