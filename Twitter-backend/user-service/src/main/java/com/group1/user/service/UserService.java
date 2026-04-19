package com.group1.user.service;

import java.util.List;

import com.group1.user.dto.LoginRequestDto;
import com.group1.user.dto.LoginResponseDto;
import com.group1.user.dto.ProfileUpdateDto;
import com.group1.user.dto.UserRegistrationDto;
import com.group1.user.dto.UserResponseDto;
import com.group1.user.entity.User;

public interface UserService {

	public String registerUser(UserRegistrationDto dto);
	public LoginResponseDto loginUser(LoginRequestDto dto) ;
	public User updateUserByEmail(String email,ProfileUpdateDto dto);
	public List<UserResponseDto> searchUsersByName(String prefix);
	public UserResponseDto getUserProfile(Long id);
	String generatePasswordResetToken(String email);
	String resetPassword(String email, String token, String newPassword);
}
