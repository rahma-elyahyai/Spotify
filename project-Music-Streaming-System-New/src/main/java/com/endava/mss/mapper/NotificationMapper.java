package com.endava.mss.mapper;
import org.springframework.stereotype.Component;

import com.endava.mss.entities.Notification;
import com.endava.mss.entityDTO.NotificationDTO;


@Component
public class NotificationMapper {


	
    public NotificationDTO NotificationtoDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .userId(notification.getUser().getId())
                .build();
    }

    public Notification DTOtoNotification(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Notification notification= Notification.builder()
                .id(dto.getId())
                .message(dto.getMessage())
                .status(dto.getStatus())
                .build();
        
     return notification;
    }
}

