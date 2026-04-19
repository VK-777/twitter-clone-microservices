package com.group1.tweetservice.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserProfileDto {

	private Long id;
	private String firstName;
	private String email;
	private String bio;
	private String location;
	private String website;
	private String profilePictureUrl;
	private String coverPictureUrl;
	private LocalDateTime createdAt;
}
