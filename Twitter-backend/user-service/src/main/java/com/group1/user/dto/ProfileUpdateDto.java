package com.group1.user.dto;

import lombok.Data;

@Data
public class ProfileUpdateDto {

	private String bio;
	private String location;
	private String website;
	private String profilePictureUrl;
	private String coverPictureUrl;
}
