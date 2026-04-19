package com.group1.user.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group1.user.dto.LoginRequestDto;
import com.group1.user.dto.LoginResponseDto;
import com.group1.user.dto.ProfileUpdateDto;
import com.group1.user.dto.UserRegistrationDto;
import com.group1.user.dto.UserResponseDto;
import com.group1.user.entity.User;
import com.group1.user.exception.InvalidCredentialsException;
import com.group1.user.exception.ResourceNotFoundException;
import com.group1.user.exception.UserAlreadyExistsException;
import com.group1.user.repository.UserRepository;
import com.group1.user.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private static final String USER_NOT_FOUND="User not found";
	public UserServiceImpl(UserRepository userRepository,ModelMapper modelMapper,PasswordEncoder passwordEncoder,JwtUtil jwtUtil){
		this.userRepository=userRepository;
		this.modelMapper=modelMapper;
		this.passwordEncoder=passwordEncoder;
		this.jwtUtil=jwtUtil;
	}
		
	@Override
	public String registerUser(UserRegistrationDto dto) {
		if(!dto.getPassword().equals(dto.getConfirmPassword())) {
			throw new IllegalArgumentException("Passwords do not match"); 
		}
		if(userRepository.existsByEmail(dto.getEmail())) {
			throw new UserAlreadyExistsException("Email is already associated with an existing account");
		}
		User user=modelMapper.map(dto, User.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return "Account successfully created!";
	}
	@Override
	public LoginResponseDto loginUser(LoginRequestDto dto) {
		User user=userRepository.findByEmail(dto.getEmail()).orElseThrow(()->new InvalidCredentialsException("Invalid credentials"));
		if(passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			String token=jwtUtil.generateToken(dto.getEmail());
			UserResponseDto userResponseDto=modelMapper.map(user, UserResponseDto.class);
			return new LoginResponseDto(token,userResponseDto);
		}
		else {
			throw new InvalidCredentialsException("Invalid credentials");
		}
	}
	@Override
	public User updateUserByEmail(String email,ProfileUpdateDto dto) {
		User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException(USER_NOT_FOUND));
		if(dto.getBio()!=null)user.setBio(dto.getBio());
		if(dto.getLocation()!=null)user.setLocation(dto.getLocation());
		if(dto.getWebsite()!=null)user.setWebsite(dto.getWebsite());
		if(dto.getProfilePictureUrl()!=null)user.setProfilePictureUrl(dto.getProfilePictureUrl());
		if(dto.getCoverPictureUrl()!=null)user.setCoverPictureUrl(dto.getCoverPictureUrl());
		return userRepository.save(user);
	}
	
	@Override
	public List<UserResponseDto> searchUsersByName(String prefix){
		List<User> users;
		if(prefix==null||prefix.trim().isEmpty()) {
			users=userRepository.findAll();
		}
		else {
			users=userRepository.findByFirstNameStartingWithIgnoreCase(prefix);
		}
		return users.stream().map(user->modelMapper.map(user, UserResponseDto.class)).toList();
	}
	@Override
	public UserResponseDto getUserProfile(Long id) {
		User user=userRepository.findById(id).orElseThrow(()->new RuntimeException(USER_NOT_FOUND));
		return modelMapper.map(user, UserResponseDto.class);
	}
	@Override
    public String generatePasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        
        // Generate a random 6-character code
        String token = java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        user.setResetToken(token);
        userRepository.save(user);
        
        return token; 
    }

    @Override
    public String resetPassword(String email, String token, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
                
        if (user.getResetToken() != null && user.getResetToken().equals(token)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null); // Clear token after use
            userRepository.save(user);
            return "Password successfully reset.";
        } else {
            throw new InvalidCredentialsException("Invalid or expired reset token.");
        }
    }
}
