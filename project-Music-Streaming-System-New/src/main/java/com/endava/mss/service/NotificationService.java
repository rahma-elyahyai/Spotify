package com.endava.mss.service;

import java.util.List;

import com.endava.mss.entityDTO.NotificationDTO;

public interface NotificationService {

	NotificationDTO createNewNotification(NotificationDTO notificationDTO);
	
	List<NotificationDTO>getAllNotifications(Long userId);
	
	void clearNotifications(Long userId);
	
}
