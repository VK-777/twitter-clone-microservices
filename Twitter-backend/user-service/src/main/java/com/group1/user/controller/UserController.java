package com.group1.user.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group1.user.dto.LoginRequestDto;
import com.group1.user.dto.LoginResponseDto;
import com.group1.user.dto.ProfileUpdateDto;
import com.group1.user.dto.UserRegistrationDto;
import com.group1.user.dto.UserResponseDto;
import com.group1.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	public final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto dto){
		String response=userService.registerUser(dto);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginRequestDto dto){
		LoginResponseDto requestDto = userService.loginUser(dto);
		return new ResponseEntity<>(requestDto,HttpStatus.OK);
		
	}
	@PutMapping("/profile")
	public ResponseEntity<String> updateProfile(@RequestHeader("X-Logged-In-User") String email,@RequestBody ProfileUpdateDto dto){
		userService.updateUserByEmail(email, dto);
		return new ResponseEntity<>("Profile updated successfully",HttpStatus.OK);
	}
	@GetMapping("/search")
	public ResponseEntity<List<UserResponseDto>> searchUserNameByFirstName(@RequestParam String prefix){
		List<UserResponseDto> users=userService.searchUsersByName(prefix);
		return new ResponseEntity<>(users,HttpStatus.OK);
	}
	@GetMapping("{id}/profile")
	public ResponseEntity<UserResponseDto> viewProfile(@PathVariable Long id){
		UserResponseDto userProfile=userService.getUserProfile(id);
		return new ResponseEntity<>(userProfile,HttpStatus.OK);
	}
	@PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String token = userService.generatePasswordResetToken(email);
        return new ResponseEntity<>("Mock email sent. Token: " + token, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email, 
            @RequestParam String token, 
            @RequestParam String newPassword) {
        return new ResponseEntity<>(userService.resetPassword(email, token, newPassword), HttpStatus.OK);
    }
}
