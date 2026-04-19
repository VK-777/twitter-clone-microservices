package com.group1.user;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.group1.user.service.UserServiceImpl;
import com.group1.user.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserMsApplicationTests {

	@Mock
	private UserRepository userRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;
	
	@InjectMocks 
	private UserServiceImpl userServiceImpl;
	private User user;
	private UserRegistrationDto dto;
	
	@BeforeEach
	void setUp() {
		user=new User();
		user.setId(1L);
		user.setEmail("test@in");
		user.setPassword("hashedPass");
		dto=new UserRegistrationDto();
		dto.setFirstName("Test");
		dto.setEmail("test@in");
		dto.setPassword("Pass@123");
		dto.setConfirmPassword("Pass@123");
	}
	
	@Test
	void userRegisterSuccess() {
		Mockito.when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
		Mockito.when(modelMapper.map(dto, User.class)).thenReturn(user);
		Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("hashedPass");
		String response=userServiceImpl.registerUser(dto);
		Assertions.assertEquals("Account successfully created!", response);
		Mockito.verify(userRepository,Mockito.times(1)).save(Mockito.any(User.class));
	}
	@Test
	void userRegisterFail_UserExists() {
		Mockito.when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
		Assertions.assertThrows(UserAlreadyExistsException.class, ()->userServiceImpl.registerUser(dto));
	}
	@Test
	void userRegisterFail_PasswordWrong() {
		dto.setConfirmPassword("WrongPassword@123");
		Assertions.assertThrows(IllegalArgumentException.class, ()->userServiceImpl.registerUser(dto));
	}
	@Test
	void userLoginSuccess() {
		LoginRequestDto loginDto=new LoginRequestDto();
		loginDto.setEmail("test@in");
		loginDto.setPassword("Pass@123");
		
		Mockito.when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
		Mockito.when(passwordEncoder.matches(loginDto.getPassword(),user.getPassword())).thenReturn(true);
		Mockito.when(jwtUtil.generateToken(loginDto.getEmail())).thenReturn("mockToken");
		LoginResponseDto token=userServiceImpl.loginUser(loginDto);
		Assertions.assertEquals("mockToken", token.getToken());
	}
	@Test
	void userLoginFailInvalidCredentials() {
		LoginRequestDto loginDto=new LoginRequestDto();
		loginDto.setEmail("test@in");
		loginDto.setPassword("WrongPass@123");
		
		Mockito.when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
		Mockito.when(passwordEncoder.matches(loginDto.getPassword(),user.getPassword())).thenReturn(false);
		
		Assertions.assertThrows(InvalidCredentialsException.class, ()->userServiceImpl.loginUser(loginDto));
	}
	@Test
	void userProfileUpdateSuccess() {
		ProfileUpdateDto profileUpdateDto=new ProfileUpdateDto();
		profileUpdateDto.setBio("New Bio");
		
		Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		
		User updatedUser=userServiceImpl.updateUserByEmail("test@in", profileUpdateDto);
		
		Assertions.assertEquals("New Bio", updatedUser.getBio());
		Mockito.verify(userRepository,Mockito.times(1)).save(user);
	}
	@Test
	void userProfileUpdateAllFieldsNull() {
		ProfileUpdateDto profileUpdateDto=new ProfileUpdateDto();
		User existing=new User();
		existing.setBio("Old Bio");
		existing.setLocation("Old Location");
		existing.setWebsite("Old Website");
		existing.setProfilePictureUrl("oldPic.jpg");
		existing.setCoverPictureUrl("oldCover.jpg");
		Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(existing));
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i->i.getArgument(0));
		
		User updatedUser=userServiceImpl.updateUserByEmail("test@in", profileUpdateDto);
		
		Assertions.assertEquals("Old Bio", updatedUser.getBio());
		Assertions.assertEquals("Old Website", updatedUser.getWebsite());
		Assertions.assertEquals("Old Location", updatedUser.getLocation());
		Assertions.assertEquals("oldPic.jpg", updatedUser.getProfilePictureUrl());
		Assertions.assertEquals("oldCover.jpg", updatedUser.getCoverPictureUrl());
		
	}
	@Test
	void userProfileUpdateAllFieldsUpdated() {
		ProfileUpdateDto profileUpdateDto=new ProfileUpdateDto();
		
		profileUpdateDto.setBio("New Bio");
		profileUpdateDto.setLocation("New Location");
		profileUpdateDto.setWebsite("New Website");
		profileUpdateDto.setProfilePictureUrl("newPic.jpg");
		profileUpdateDto.setCoverPictureUrl("newCover.jpg");
		User existing=new User();
		Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(existing));
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i->i.getArgument(0));
		
		User updatedUser=userServiceImpl.updateUserByEmail("test@in", profileUpdateDto);
		
		Assertions.assertEquals("New Bio", updatedUser.getBio());
		Assertions.assertEquals("New Website", updatedUser.getWebsite());
		Assertions.assertEquals("New Location", updatedUser.getLocation());
		Assertions.assertEquals("newPic.jpg", updatedUser.getProfilePictureUrl());
		Assertions.assertEquals("newCover.jpg", updatedUser.getCoverPictureUrl());
		
	}
	@Test
	void userProfileNotFound() {
		ProfileUpdateDto profileUpdateDto=new ProfileUpdateDto();
		Mockito.when(userRepository.findByEmail("notfound@in")).thenReturn(Optional.empty());
		Assertions.assertThrows(ResourceNotFoundException.class, ()->userServiceImpl.updateUserByEmail("notfound@in", profileUpdateDto));
	}
	@Test
	void searchUserByName() {
		Mockito.when(userRepository.findByFirstNameStartingWithIgnoreCase("Te")).thenReturn(List.of(user));
		Mockito.when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(new UserResponseDto());
		List<UserResponseDto> res=userServiceImpl.searchUsersByName("Te");
		Assertions.assertFalse(res.isEmpty());
		Mockito.verify(userRepository,Mockito.times(1)).findByFirstNameStartingWithIgnoreCase("Te");
	}
	@Test
	void searchUserByEmptyName() {
		Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
		Mockito.when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(new UserResponseDto());
		List<UserResponseDto> res=userServiceImpl.searchUsersByName("");
		Assertions.assertFalse(res.isEmpty());
		Mockito.verify(userRepository,Mockito.times(1)).findAll();
	}
	@Test
	void searchUserByNullName() {
		Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
		Mockito.when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(new UserResponseDto());
		List<UserResponseDto> res=userServiceImpl.searchUsersByName(null);
		Assertions.assertFalse(res.isEmpty());
		Mockito.verify(userRepository,Mockito.times(1)).findAll();
	}
	@Test
	void searchUserProfile() {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		Mockito.when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(new UserResponseDto());
		UserResponseDto res=userServiceImpl.getUserProfile(1L);
		Assertions.assertNotNull(res);
		Mockito.verify(userRepository,Mockito.times(1)).findById(1L);
	}
	@Test
    void testResetPassword_Success() {
		user.setResetToken("A1B2C3");
		Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(user));
		Mockito.when(passwordEncoder.encode("NewPass@123")).thenReturn("hashedPass");

        String response = userServiceImpl.resetPassword("test@in", "A1B2C3", "NewPass@123");

        Assertions.assertEquals("Password successfully reset.", response);
        Assertions.assertNull(user.getResetToken()); // Ensure the token is wiped out
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    // 2. The Null Token Path (False condition - branch 1)
    @Test
    void testResetPassword_TokenIsNull_ThrowsException() {
    	user.setResetToken(null); // The user hasn't requested a reset yet
    	Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidCredentialsException.class, () -> 
        	userServiceImpl.resetPassword("test@in", "A1B2C3", "NewPass@123")
        );
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    // 3. The Wrong Token Path (False condition - branch 2)
    @Test
    void testResetPassword_TokenMismatch_ThrowsException() {
    	user.setResetToken("A1B2C3"); // The real token
    	Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(user));

        // Attempting to use the WRONG token ("WRONG")
        Assertions.assertThrows(InvalidCredentialsException.class, () -> 
        	userServiceImpl.resetPassword("test@in", "WRONG", "NewPass@123")
        );
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    
    @Test
    void testResetPassword_InvalidToken_ThrowsException() {
        user.setResetToken("A1B2C3");
        Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidCredentialsException.class, () -> 
        	userServiceImpl.resetPassword("test@in", "WRONG", "NewPass@123")
        );
    }
    @Test
    void testGeneratePasswordResetToken_Success() {
    	Mockito.when(userRepository.findByEmail("test@in")).thenReturn(Optional.of(user));
    	Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        String token = userServiceImpl.generatePasswordResetToken("test@in");
        
        Assertions.assertNotNull(token);
        Assertions.assertEquals(6, token.length());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }
}
