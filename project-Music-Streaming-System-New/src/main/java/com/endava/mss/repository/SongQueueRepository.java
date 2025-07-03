package com.endava.mss.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.SongQueue;
import com.endava.mss.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface SongQueueRepository extends JpaRepository<SongQueue,Long>{

	 @Query("SELECT ls FROM SongQueue ls WHERE ls.user = ?1")
	    List<SongQueue> getSongQueueByUser(User user);
	 
	    @Transactional
	    @Modifying
	    @Query(value = "DELETE FROM song_queue WHERE user_id=?1", 
	    nativeQuery = true)
	    void clearUserSongQueue( Long userid);
	    
	    @Query(value= "select song_id from song_queue where user_id=?1 and current_position=?2",
	    		nativeQuery = true)
	    Long getSongFromCurrentPosition(Long userid,Long currentPosition);
	    
	    @Query(value = "select count(*) from song_queue where user_id = ?1 AND song_id = ?2", nativeQuery = true)
	    Long existsSongInUserQueue(Long userId, Long songId);

}
