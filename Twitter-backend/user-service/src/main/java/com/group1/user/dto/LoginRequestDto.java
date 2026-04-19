package com.group1.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

	@NotBlank(message="Please provide a valid email")
	private String email;
	
	@NotBlank(message="Please provide a valid password")
	private String password;
	
}
