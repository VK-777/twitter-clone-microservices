package com.group1.search.client;

import com.group1.search.dto.UserResponseDTO;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/search")

    List<UserResponseDTO> searchUsers(@RequestParam("prefix") String prefix);

}

