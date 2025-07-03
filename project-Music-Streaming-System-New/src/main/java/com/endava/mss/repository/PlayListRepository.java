package com.endava.mss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.PlayList;

import jakarta.transaction.Transactional;

@Repository
public interface PlayListRepository extends JpaRepository<PlayList, Long> {


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM play_list_songs WHERE play_list_id = ?1 AND song_id = ?2", 
    nativeQuery = true)
    void deleteSongFromPlaylist( Long playlistId, Long songId);
}
