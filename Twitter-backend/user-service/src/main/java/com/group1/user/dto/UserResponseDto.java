package com.group1.user.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponseDto {

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
