package com.endava.mss.entities;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListeningHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	

    @Column(nullable = false, updatable = false)
    private LocalDateTime timeStamp; 
    
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.timeStamp = now;
    }
    
    private Long durationPlayed;
    
    @ManyToOne()
    @JoinColumn(name="song_id")
    private Song song;
    
    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;

}
