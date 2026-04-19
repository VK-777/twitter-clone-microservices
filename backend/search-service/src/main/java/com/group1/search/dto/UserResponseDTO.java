package com.group1.search.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

	private Long id;
	private String firstName;
	private String email;
	private String bio;
	private String location;
	private String website;
	private String profilePictureUrl;
	private String coverPictureUrl;

}