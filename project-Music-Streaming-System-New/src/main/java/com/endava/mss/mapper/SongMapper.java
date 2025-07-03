package com.endava.mss.mapper;

import org.springframework.stereotype.Component;

import com.endava.mss.entities.Song;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.entityDTO.SongInfoDTO;

@Component
public class SongMapper {

	public SongDTO SongtoDto(Song song) {
		return SongDTO.builder().id(song.getId()).title(song.getTitle()).genre(song.getGenre())
				.language(song.getLanguage()).playCount(song.getPlayCount()).coverImage(song.getCoverImage())
				.releaseDate(song.getReleaseDate()).coverImage(song.getCoverImage()).mp3File(song.getMp3File())
				.favorite(song.getFavorite()).status(song.getStatus()).lyrics(song.getLyrics())
				.albumId(song.getAlbum().getId()).artistId(song.getArtist().getId())
				.listenedDuration(song.getListenedDuration()).build();
	}

	public Song DTOtoSong(SongDTO songDTO) {
		return Song.builder().id(songDTO.getId()).title(songDTO.getTitle()).genre(songDTO.getGenre())
				.language(songDTO.getLanguage()).playCount(songDTO.getPlayCount()).coverImage(songDTO.getCoverImage())
				.mp3File(songDTO.getMp3File()).releaseDate(songDTO.getReleaseDate()).favorite(songDTO.getFavorite())
				.status(songDTO.getStatus()).lyrics(songDTO.getLyrics()).build();
	}

	public SongInfoDTO songtoSongInfo(Song song) {

		return new SongInfoDTO(song.getId(), song.getTitle(), song.getPlayCount(), song.getStatus(),
				song.getArtist().getId(), song.getLyrics(), song.getGenre(), song.getLanguage(),
				song.getAlbum().getId(), song.getReleaseDate());
	}
}
