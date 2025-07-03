package com.endava.mss.entityDTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class UserDTO {


    private Long id;
    private String name;
    private String email;
    private String password;
    private String location;
    private String preferences;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<PlayListDTO>playlists;
     
    private List<NotificationDTO>notifications;
 
} 
