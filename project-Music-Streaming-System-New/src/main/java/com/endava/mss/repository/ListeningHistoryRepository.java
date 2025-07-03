package com.endava.mss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.ListeningHistory;
import com.endava.mss.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory,Long> {

	 @Query("SELECT lh FROM ListeningHistory lh WHERE lh.user = ?1")
	    List<ListeningHistory> getListeningHistoryByUser(User user);
	 
//	 	List<ListeningHistory> findByUser(User user);
	 
	   @Transactional
	    @Modifying
	    @Query(value = "DELETE FROM listening_history WHERE user_id=?1", 
	    nativeQuery = true)
	    void deleteListeningHistory( Long userid);
}
