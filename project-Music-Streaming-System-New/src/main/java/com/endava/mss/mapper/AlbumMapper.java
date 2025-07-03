package com.endava.mss.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.Album;
import com.endava.mss.entities.Artist;
import com.endava.mss.entityDTO.AlbumDTO;
import com.endava.mss.entityDTO.AlbumInfoDTO;
import com.endava.mss.repository.ArtistRepository;

@Component
public class AlbumMapper {

	@Autowired
	private ArtistRepository artistRepository;
	@Autowired
	private SongMapper songMapper;
	
	
	public Album  AlbumDTOtoAlbum(AlbumDTO albumDTO)
	{
		Artist artist = artistRepository.findById(albumDTO.getArtistId()).orElse(null);
		
		Album album = Album.builder().id(albumDTO.getId()).artist(artist).coverImage(albumDTO.getCoverImage())
				.releaseDate(albumDTO.getReleaseDate()).title(albumDTO.getTitle()).build();
		
		album.setArtist(artist);
		
		return album;
	}
	public AlbumDTO albumTAlbumDTO(Album album)
	{
		AlbumDTO albumDTO = AlbumDTO.builder().id(album.getId()).artistId(album.getArtist().getId())
				.releaseDate(album.getReleaseDate()).coverImage(album.getCoverImage())
				.title(album.getTitle())
			     .build();
		
		if(album.getSongs()!=null && !album.getSongs().isEmpty())
		{
			albumDTO.setSongs(album.getSongs().stream().map(p->songMapper.SongtoDto(p)).toList());
		}
		
		return albumDTO;
	}
	
	public AlbumInfoDTO albumToAlbumInfoDTO(Album album)
	{
		return AlbumInfoDTO.builder().id(album.getId()).artistId(album.getArtist().getId()).title(album.getTitle())
				.songs(album.getSongs().stream().map(s->songMapper.songtoSongInfo(s)).toList()).build();
	}
	
}
