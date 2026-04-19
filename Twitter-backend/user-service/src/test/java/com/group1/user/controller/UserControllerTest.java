package com.group1.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group1.user.dto.LoginRequestDto;
import com.group1.user.dto.LoginResponseDto;
import com.group1.user.dto.ProfileUpdateDto;
import com.group1.user.dto.UserRegistrationDto;
import com.group1.user.dto.UserResponseDto;
import com.group1.user.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters=false)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserService userService;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void registerUser_Created() throws Exception{
		UserRegistrationDto dto=new UserRegistrationDto();
		dto.setFirstName("Test Name");
		dto.setEmail("test@in");
		dto.setPassword("Pass@123");
		dto.setConfirmPassword("Pass@123");
		when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn("Account successfully created.");
		mockMvc.perform(post("/api/v1/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated());
	}
	@Test
	void loginUser() throws Exception{
		LoginRequestDto dto=new LoginRequestDto();
		dto.setEmail("test@in");
		dto.setPassword("Pass@123");
		LoginResponseDto responseDto=new LoginResponseDto("token",new UserResponseDto());
		when(userService.loginUser(any(LoginRequestDto.class))).thenReturn(responseDto);
		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	}
	@Test
	void updateProfile() throws Exception{
		ProfileUpdateDto dto=new ProfileUpdateDto();
		dto.setBio("Bio");
		mockMvc.perform(put("/api/v1/users/profile")
				.header("X-Logged-In-User", "test@in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	}
	@Test
	void searchUsers() throws Exception{
		when(userService.searchUsersByName("Te")).thenReturn(List.of(new UserResponseDto()));
		mockMvc.perform(get("/api/v1/users/search")
				.param("prefix", "Te")).andExpect(status().isOk());
	}
	@Test
	void vieweProfile() throws Exception{
		when(userService.getUserProfile(1L)).thenReturn(new UserResponseDto());
		mockMvc.perform(get("/api/v1/users/1/profile")).andExpect(status().isOk());
	}
	@Test
    void forgotPassword_ShouldReturnToken() throws Exception {
        // Mock the service to return a fake token
        when(userService.generatePasswordResetToken("test@test.in")).thenReturn("A1B2C3");

        mockMvc.perform(post("/api/v1/users/forgot-password")
                .param("email", "test@test.in"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mock email sent. Token: A1B2C3"));
    }

    @Test
    void resetPassword_ShouldReturnSuccess() throws Exception {
        // Mock the service to return the success message
        when(userService.resetPassword("test@test.in", "A1B2C3", "NewPass@123"))
                .thenReturn("Password successfully reset.");

        mockMvc.perform(post("/api/v1/users/reset-password")
                .param("email", "test@test.in")
                .param("token", "A1B2C3")
                .param("newPassword", "NewPass@123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password successfully reset."));
    }
}
