package com.endava.mss.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name ;
	
	 @Column(nullable = false, updatable = false)
	    private LocalDateTime createdAt;
	 
	 @PrePersist
	    public void prePersist() {
	        LocalDateTime now = LocalDateTime.now();
	        this.createdAt = now;
	 }
	 
	 @ManyToOne
	 @JoinColumn(name="user_Id")
	 private User user;
	 
	 @ManyToMany
	 @JoinTable(name="playList_songs",
	 joinColumns = @JoinColumn(name="playList_Id"),
	 inverseJoinColumns = @JoinColumn(name="song_Id"))
	 private List<Song>songs;
}
