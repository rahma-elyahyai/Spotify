package com.endava.mss.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.Account;
import com.endava.mss.entities.Artist;
import com.endava.mss.entityDTO.ArtistDTO;
import com.endava.mss.entityDTO.ArtistInfoDTO;

@Component
public class ArtistMapper {

	@Autowired
	SongMapper songMapper;

	@Autowired
	AlbumMapper albumMapper;

	@Autowired
	UserMapper userMapper;

	public ArtistDTO artistToDTO(Artist artist) {
		ArtistDTO artistDTO = ArtistDTO.builder().id(artist.getId()).name(artist.getName()).bio(artist.getBio())
				.profileImage(artist.getProfileImage()).status(artist.getStatus()).email(artist.getAccount().getEmail())
				.password(artist.getAccount().getPassword()).socialLinks(artist.getSocialLinks())
				.totalPlayCount(artist.getTotalPlayCount()).build();
		if (artist.getSongs() != null && !artist.getSongs().isEmpty()) {
			artistDTO.setSongs(artist.getSongs().stream().map(p -> songMapper.songtoSongInfo(p)).toList());
		}
		if (artist.getAlbums() != null && !artist.getAlbums().isEmpty()) {
			artistDTO.setAlbums(artist.getAlbums().stream().map(p -> albumMapper.albumToAlbumInfoDTO(p)).toList());
		}
		if (artist.getUsers() != null && !artist.getUsers().isEmpty()) {
			artistDTO.setUsers(artist.getUsers().stream().map(p -> userMapper.usertoInfoDTO(p)).toList());
		}
		return artistDTO;

	} 

	public Artist DtoToArtist(ArtistDTO artistDTO) {
		return Artist.builder().Id(artistDTO.getId()).name(artistDTO.getName()).bio(artistDTO.getBio())
				.profileImage(artistDTO.getProfileImage()).status(artistDTO.getStatus())
				.account(Account.builder().email(artistDTO.getEmail()).password(artistDTO.getPassword()).build())
				.socialLinks(artistDTO.getSocialLinks()).build();
	}

	public ArtistInfoDTO artistToArtistInfo(Artist artist) {
		return new ArtistInfoDTO(artist.getId(), artist.getName());
	}
}
