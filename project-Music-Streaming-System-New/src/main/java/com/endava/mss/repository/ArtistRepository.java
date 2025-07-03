package com.endava.mss.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.Artist;


@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByAccount_Email(String email);
    boolean existsByAccount_Email(String email);

	Page<Artist> findByNameContainingIgnoreCase(String term, Pageable pageable);
	
	@Query(value="select top 5 * from artist a order by a.total_play_count desc",nativeQuery = true)
	List<Artist> findTopFiveArtists();
}
