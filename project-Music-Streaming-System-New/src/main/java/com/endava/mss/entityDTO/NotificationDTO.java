package com.endava.mss.entityDTO;
import com.endava.mss.entities.Notification.readStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {

	private Long id;

	private String message;

	private readStatus status;

	private Long userId;

}
