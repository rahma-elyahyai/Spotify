package com.endava.mss.entityDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDTO {
	private Long id;
	private String name;
	private String email;
	private String password;
	private List<PlayListDTO> playlists;
	private List<NotificationDTO> notifications;
}
