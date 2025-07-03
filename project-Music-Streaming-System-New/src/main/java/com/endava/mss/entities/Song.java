package com.endava.mss.entities;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String title;

    private String genre;

    private String language;

    private Long playCount;

    @Lob
    private byte[] mp3File; 

    private LocalDate releaseDate; 
    
    private Long favorite;

    @Enumerated(EnumType.STRING)
    private SongStatus status; // song needs to be verified by the Admin to be displayed in the webiste

    @Lob
    private byte[] coverImage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt; 
    
    @Lob
    private String lyrics;
    
    @Builder.Default
    private Long listenedDuration=0l;

    public enum SongStatus {
        PENDING, APPROVED, REJECTED
    }
    
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.submittedAt = now;
    }
    
    @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;
    
    @ManyToOne
    @JoinColumn(name="albumId",nullable = true)
    private Album album;
    
    @ManyToOne
    @JoinColumn(name="artist_id")
    private Artist artist;
    
    @ManyToMany
    private List<PlayList>playlists;
    
    @OneToMany(mappedBy = "song",cascade = CascadeType.REMOVE)
    private List<SongQueue>songQueues;
    
   
}
