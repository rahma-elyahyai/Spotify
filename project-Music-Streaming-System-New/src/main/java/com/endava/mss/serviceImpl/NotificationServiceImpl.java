package com.endava.mss.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endava.mss.constantFiles.NotificationConstants;
import com.endava.mss.entities.Notification;
import com.endava.mss.entities.User;
import com.endava.mss.entityDTO.NotificationDTO;
import com.endava.mss.exception.UserNotFoundException;
import com.endava.mss.mapper.NotificationMapper;
import com.endava.mss.repository.NotificationRepository;
import com.endava.mss.repository.UserRepository;
import com.endava.mss.service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

	
	private final NotificationRepository notificationRepository;


	private final UserRepository userRepository;

	private final NotificationMapper notificationMapper;

	
	public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository,
			NotificationMapper notificationMapper) {

		this.notificationRepository = notificationRepository;
		this.userRepository = userRepository;
		this.notificationMapper = notificationMapper;
	}

	/**
	 * Creates a new notification for a user.
	 *
	 * @param notificationDTO DTO containing notification details.
	 * @return NotificationDTO representing the saved notification.
	 * @throws UserNotFoundException if the user does not exist.
	 */
	@Override
	public NotificationDTO createNewNotification(NotificationDTO notificationDTO) {

		User user = userRepository.findById(notificationDTO.getUserId())
				.orElseThrow(() -> new UserNotFoundException(NotificationConstants.USER_NOT_FOUND_ERROR_MESSAGE));
		Notification notification = notificationMapper.DTOtoNotification(notificationDTO);
		notification.setUser(user);
		notificationRepository.save(notification);

		return notificationMapper.NotificationtoDTO(notification);
	}

	/**
	 * Retrieves all notifications for a specific user.
	 *
	 * @param userId ID of the user whose notifications are being fetched.
	 * @return List of NotificationDTOs containing user notifications.
	 * @throws UserNotFoundException if the user does not exist.
	 */
	@Override
	public List<NotificationDTO> getAllNotifications(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(NotificationConstants.USER_NOT_FOUND_ERROR_MESSAGE));

		List<NotificationDTO> userNotifications = notificationRepository.getNotificationsByUser(user).stream()
				.map(n -> notificationMapper.NotificationtoDTO(n)).collect(Collectors.toList());

		return userNotifications;
	}

	/**
	 * Clears all notifications for a specific user.
	 *
	 * @param userId ID of the user whose notifications should be cleared.
	 * @throws UserNotFoundException if the user does not exist.
	 */
	@Override
	public void clearNotifications(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(NotificationConstants.USER_NOT_FOUND_ERROR_MESSAGE));

		notificationRepository.clearUserNotifications(userId);

	}

}
