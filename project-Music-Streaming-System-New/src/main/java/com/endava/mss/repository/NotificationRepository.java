package com.endava.mss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.endava.mss.entities.Notification;
import com.endava.mss.entities.SongQueue;
import com.endava.mss.entities.User;

import jakarta.transaction.Transactional;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	 @Query("SELECT n FROM Notification n WHERE n.user = ?1")
	    List<Notification> getNotificationsByUser(User user);
	 
	   @Transactional
	    @Modifying
	    @Query(value = "DELETE FROM notification WHERE user_id=?1", 
	    nativeQuery = true)
	    void clearUserNotifications( Long userid);
	    
}
