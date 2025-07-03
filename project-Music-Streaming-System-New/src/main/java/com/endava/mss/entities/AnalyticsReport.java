package com.endava.mss.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
public class AnalyticsReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	 @Column(nullable = false, updatable = false)
	    private LocalDateTime generatedAt;
	 
	   @PrePersist
	    public void prePersist() {
	        LocalDateTime now = LocalDateTime.now();
	        this.generatedAt = now;
	    
	    }
	   
	    @Lob
	    private byte[] reportData;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="adminId")
	private Admin admin;
	

}
