package com.group1.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationDto {

	@NotBlank(message="Please provide a first name")
	@Pattern(regexp="[A-Z][a-zA-Z]*( [A-Z][A-Za-z]*)*",message="Please provide a valid first name")
	private String firstName;
	
	@NotBlank(message="Please provide a email address")
	@Email(message="Please provide a valid email address")
	private String email;
	
	@NotBlank(message="Please provide a password")
	@Pattern(regexp="(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,16}",message="Please provide a valid password")
	private String password;
	
	@NotBlank(message="Please provide a valid confirm password")
	private String confirmPassword;
}
