package com.endava.mss.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.endava.mss.constantFiles.NotificationConstants;
import com.endava.mss.entityDTO.NotificationDTO;
import com.endava.mss.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping(NotificationConstants.BASE_URL)
public class NotificationController {


	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		super();
		this.notificationService = notificationService;
	}

	@PostMapping(NotificationConstants.CREATE_NOTIFICATION)
	public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {

		NotificationDTO createdNotification = notificationService.createNewNotification(notificationDTO);
		return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);

	}

	@GetMapping(NotificationConstants.GET_NOTIFICATIONS)
	public ResponseEntity<List<NotificationDTO> > getNotifications(@PathVariable Long userId) {

		List<NotificationDTO> notifications = notificationService.getAllNotifications(userId);
		return new ResponseEntity<>(notifications, HttpStatus.OK);

	}

	@DeleteMapping(NotificationConstants.CLEAR_NOTIFICATIONS)
	public ResponseEntity<String> clearUserNotifications(@PathVariable Long userId) {

		notificationService.clearNotifications(userId);
		return new ResponseEntity<>(NotificationConstants.CLEARED_NOTIFICATIONS, HttpStatus.OK);

	}
}
