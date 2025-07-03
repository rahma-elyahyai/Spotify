package com.endava.mss.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.User;
import com.endava.mss.entities.Account;
import com.endava.mss.entities.Notification.readStatus;
import com.endava.mss.entityDTO.UserDTO;
import com.endava.mss.entityDTO.UserInfoDTO;

@Component
public class UserMapper {

	@Autowired
	private PlayListMapper playListMapper;

	@Autowired
	private NotificationMapper notificationMapper;

	public UserDTO userToUserDTO(User user) {
		if (user == null) {
			return null;
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setName(user.getName());
		userDTO.setEmail(user.getAccount().getEmail());
		userDTO.setPassword(user.getAccount().getPassword());
		userDTO.setLocation(user.getLocation());
		userDTO.setPreferences(user.getPreferences());
		userDTO.setCreatedAt(user.getAccount().getCreatedAt());
		userDTO.setUpdatedAt(user.getAccount().getUpdatedAt());
		if (user.getPlayLists() != null) {
			userDTO.setPlaylists(user.getPlayLists().stream().map(p -> playListMapper.PlayListToDTO(p)).toList());
		}
		if (user.getNotifications() != null) {
			userDTO.setNotifications(user.getNotifications().stream().map(n -> notificationMapper.NotificationtoDTO(n))
					.collect(Collectors.toList()));
		}

		return userDTO;
	}

	public User userDTOToUser(UserDTO userDTO) {
		if (userDTO == null) {
			return null;
		}
		User user = new User();
		user.setId(userDTO.getId());
		user.setName(userDTO.getName());
		user.setAccount(Account.builder().email(userDTO.getEmail()).password(userDTO.getPassword())
				.createdAt(userDTO.getCreatedAt()).updatedAt(userDTO.getUpdatedAt()).build());
		user.setLocation(userDTO.getLocation());
		user.setPreferences(userDTO.getPreferences());

		return user;
	}

	public UserInfoDTO usertoInfoDTO(User user) {
		UserInfoDTO userInfoDTO = UserInfoDTO.builder().id(user.getId()).name(user.getName())
				.password(user.getAccount().getPassword()).email(user.getAccount().getEmail()).build();

		if (user.getPlayLists() != null) {
			userInfoDTO.setPlaylists(user.getPlayLists().stream().map(p -> playListMapper.PlayListToDTO(p)).toList());
		}
		if (user.getNotifications() != null) {
			userInfoDTO.setNotifications(user.getNotifications().stream()
					.map(n -> notificationMapper.NotificationtoDTO(n)).collect(Collectors.toList()));
		}
		return userInfoDTO;
	}

}
