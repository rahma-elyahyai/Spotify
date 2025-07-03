package com.endava.mss.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Album {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id  ;
	
	private String title ;
	
	private LocalDate releaseDate ;
	
    @Lob
    private byte[] coverImage;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="artist_id")
	private Artist artist;
    
    @OneToMany(mappedBy = "album" ,cascade = CascadeType.ALL )
    private  List<Song> songs;

}
