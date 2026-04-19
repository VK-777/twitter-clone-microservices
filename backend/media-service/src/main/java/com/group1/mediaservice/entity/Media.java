package com.group1.mediaservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "media")
public class Media {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String originalName;
	
	@Column(nullable = false)
	private String storedName;
	
	@Column(nullable = false)
	private String fileType;
	
	@Column(nullable = false)
	private Long fileSize;
	
	@Column(nullable = false)
	private String storagePath;
	
	@Column(nullable = false)
	private Long userId;
	
	private Long tweetId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MediaStatus status;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

}
