package com.endava.mss.entities;

import java.time.LocalDateTime;

import com.endava.mss.entities.Song.SongStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String message;
	
	  @Column(nullable = false, updatable = false)
	    private LocalDateTime createdAt;

	    private LocalDateTime updatedAt;

	    @Enumerated(EnumType.STRING)
	    private readStatus status;
	    
	    public enum readStatus {
	        READ,UNREAD
	    }
	    
	    @PrePersist
	    public void prePersist() {
	        LocalDateTime now = LocalDateTime.now();
	        this.createdAt = now;
	        this.updatedAt = now;
	    }

	    @PreUpdate
	    public void preUpdate() {
	        this.updatedAt = LocalDateTime.now();
	    }

	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name="user_Id")
	    private User user;
}
