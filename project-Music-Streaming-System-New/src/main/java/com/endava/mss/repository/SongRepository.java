package com.endava.mss.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.Song;
import com.endava.mss.entities.Song.SongStatus;
import com.endava.mss.entityDTO.SongDTO;
import com.endava.mss.entityDTO.SongInfoDTO;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

	@Query(value = "SELECT * FROM song WHERE status='APPROVED'", nativeQuery = true)
	List<Song> getAllApprovedSongs();

	@Query("SELECT s FROM Song s WHERE (?1 IS NULL OR s.genre = ?1) AND (?2 IS NULL OR s.language = ?2)")
	List<Song> findByGenreAndLanguage(String genre, String language);

	@Query("SELECT DISTINCT s.genre FROM Song s")
	List<String> findAllGenres();

	@Query("SELECT DISTINCT s.language FROM Song s")
	List<String> findAllLanguages();

	Page<Song> findByTitleContainingIgnoreCase(String term, Pageable pageable);

	@Query(value = "select * from song s where status='APPROVED' order by s.play_count desc limit 5", nativeQuery = true)
	List<Song> findTopFiveSongs();

	@Query("SELECT new com.endava.mss.entityDTO.SongDTO(s.id, s.title, s.genre, s.language, s.playCount, s.coverImage, s.releaseDate, s.status, s.favorite, s.lyrics, s.mp3File, s.artist.id, s.album.id, s.listenedDuration) FROM Song s")
	List<SongDTO> findAllSongsAsDTO();

	Page<SongInfoDTO>findByStatus(SongStatus status,Pageable pageable);

}
