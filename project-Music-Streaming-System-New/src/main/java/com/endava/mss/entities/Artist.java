package com.endava.mss.entities;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Artist {

	@jakarta.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	private String name;

	@Lob
	private String bio;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName = "id", unique = true)
	private Account account;

	@Lob
	private byte[] profileImage;

	@Enumerated(EnumType.STRING)
	private isVerified status;

	public enum isVerified {
		PENDING, APPROVED, REJECTED
	}

	private String socialLinks;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
	private List<Album> albums;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
	private List<Song> songs;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
	private List<LiveConcerts> liveConcerts;

	@ManyToMany(mappedBy = "followedArtists", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<User> users;

	@Builder.Default
	private Long totalPlayCount = 0l;
}
